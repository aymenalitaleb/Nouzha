package esi.siw.nouzha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.common.CommonStaff;
import esi.siw.nouzha.models.User;
import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnSignUp;
    TextView txtSlogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Paper
        Paper.init(this);

        btnSignIn= findViewById(R.id.btnSignIn);
        btnSignUp= findViewById(R.id.btnSignUp);

        txtSlogan=findViewById(R.id.txtSlogan);

        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Nabila.ttf");
        txtSlogan.setTypeface(face);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(signIn);

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUn = new Intent(MainActivity.this,SignUp.class);
                startActivity(signUn);

            }
        });

        if (Common.isConnectedToInternet(getBaseContext())) {
            String user = Paper.book().read(Common.USER_KEY);
            String password = Paper.book().read(Common.PWD_KEY);
            if ( user!=null && password!=null) {
                if (!user.isEmpty() && !password.isEmpty()) {
                    login(user, password);
                }
            }
        }


    }

    private void login(final String phone, final String password) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

          table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Check if user not exist in database
                if (dataSnapshot.child(phone).exists()) {

                    //Get User data
                    mDialog.dismiss();
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone); //set phone
                    if (!Boolean.parseBoolean(user.getIsStaff())) {
                        if (user.getPassword().equals(password)) {
                            Log.e("lastname", user.getLastname());
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Wrong user's password !", Toast.LENGTH_SHORT).show();
                        }
                    } else if (Boolean.parseBoolean(user.getIsStaff())) {
                        if (user.getPassword().equals(password)) {

                            Intent login = new Intent(MainActivity.this, HomeStaff.class);
                            CommonStaff.currentUser = user;
                            startActivity(login);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Wrong staff's password !", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "User not exist in Database !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

