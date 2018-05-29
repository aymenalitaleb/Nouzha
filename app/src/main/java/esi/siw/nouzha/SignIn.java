package esi.siw.nouzha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button btnSignIn;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        //Init Firebase
        final DatabaseReference table_user = database.getReference("User");

        // Init paper
        Paper.init(this);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                // Save user session
                Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());

                table_user.addValueEventListener(new ValueEventListener() {
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
                                    Log.e("lastname", user.getLastname());
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                } else {
                                    Toast.makeText(SignIn.this, "Wrong user's password !", Toast.LENGTH_SHORT).show();
                                }
                            } else if (Boolean.parseBoolean(user.getIsStaff())) {
                                if (user.getPassword().equals(edtPassword.getText().toString())) {

                                    Intent login = new Intent(SignIn.this, HomeStaff.class);
                                    CommonStaff.currentUser = user;
                                    startActivity(login);
                                    finish();

                                } else {
                                    Toast.makeText(SignIn.this, "Wrong staff's password !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist in Database !", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


}
