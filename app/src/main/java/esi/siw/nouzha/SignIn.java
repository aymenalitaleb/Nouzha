package esi.siw.nouzha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn, btnSignUp;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    LinearLayout linearLayout1, linearLayout2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);

        handler.postDelayed(runnable, 1200);

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        //Init Firebase
        final DatabaseReference table_user = database.getReference("User");

        // Init paper
        Paper.init(this);

        Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
        Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage(String.valueOf(R.string.please_wait));
                    mDialog.show();
                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Check if user not exist in database
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                                //Get User data
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(edtPhone.getText().toString()); //set phone
                                if (!Boolean.parseBoolean(user.getIsStaff())) {
                                    if (user.getPassword().equals(edtPassword.getText().toString())) {
                                        Intent homeIntent = new Intent(SignIn.this, Home.class);
                                        Common.currentUser = user;
                                        startActivity(homeIntent);
                                        finish();
                                        table_user.removeEventListener(this);
                                    } else {
                                        Toast.makeText(SignIn.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (Boolean.parseBoolean(user.getIsStaff())) {
                                    if (user.getPassword().equals(edtPassword.getText().toString())) {
                                        Intent login = new Intent(SignIn.this, HomeStaff.class);
                                        CommonStaff.currentUser = user;
                                        startActivity(login);
                                        finish();

                                    } else {
                                        Toast.makeText(SignIn.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, R.string.user_not_exist, Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                } else {
//                    Toast.makeText(SignIn.this, R.string.check_connection, Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }


}
