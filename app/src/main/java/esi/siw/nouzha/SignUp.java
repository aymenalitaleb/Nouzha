package esi.siw.nouzha;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import esi.siw.nouzha.models.User;

public class SignUp extends AppCompatActivity {

    EditText edtFirstName, edtLastName, edtEmail, edtPhone, edtPassword, edtBirthDay, edtBirthPlace, edtAvatar, edtProfession;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtBirthDay = findViewById(R.id.edtBirthDay);
        edtBirthPlace = findViewById(R.id.edtBirthPlace);
        edtAvatar = findViewById(R.id.edtAvatar);
        edtProfession = findViewById(R.id.edtProfession);
        btnSignUp = findViewById(R.id.btnSignUp);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage(String.valueOf(R.string.please_wait));
                    mDialog.show();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Phone number already used
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, R.string.phone_already_exist, Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtFirstName.getText().toString(),
                                        edtLastName.getText().toString(),
                                        edtEmail.getText().toString(),
                                        edtPassword.getText().toString(),
                                        edtBirthDay.getText().toString(),
                                        edtBirthPlace.getText().toString(),
                                        edtProfession.getText().toString(),
                                        edtAvatar.getText().toString()
                                );
                                table_user.child(edtPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, R.string.register_successfully, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(SignUp.this, R.string.check_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
