package esi.siw.nouzha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.common.CommonStaff;
import esi.siw.nouzha.models.User;
import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnSignUp;
    TextView txtSlogan;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        printHashKey();

        // Init Paper
        Paper.init(this);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        txtSlogan = findViewById(R.id.txtSlogan);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Nabila.ttf");
        txtSlogan.setTypeface(face);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(signIn);

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUn = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUn);

            }
        });

//        if (Common.isConnectedToInternet(getBaseContext())) {
        String user = Paper.book().read(Common.USER_KEY);
        String password = Paper.book().read(Common.PWD_KEY);
        if (user != null && password != null) {
            if (!user.isEmpty() && !password.isEmpty()) {
                login(user, password);
            }
        }
        //}


    }

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("esi.siw.nouzha",
                    PackageManager.GET_SIGNATURES);

            for(Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                        }
                    } else if (Boolean.parseBoolean(user.getIsStaff())) {
                        if (user.getPassword().equals(password)) {

                            Intent login = new Intent(MainActivity.this, HomeStaff.class);
                            CommonStaff.currentUser = user;
                            startActivity(login);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, R.string.user_not_exist, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

