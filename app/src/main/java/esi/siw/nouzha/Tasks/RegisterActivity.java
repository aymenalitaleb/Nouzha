package esi.siw.nouzha.Tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import esi.siw.nouzha.Dashboard;
import esi.siw.nouzha.Entities.Activity;

/**
 * Created by Creator on 25/03/2018.
 */

public class RegisterActivity extends AsyncTask {

    private Context context;


    public RegisterActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
    }

    @Override
    protected Object doInBackground(Object[] arg0) {
        try {

            // These inputs are sent via the post method
            String email = (String) arg0[0];
            String password = (String) arg0[1];
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((android.app.Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(context, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(context, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                context.startActivity(new Intent(context, Dashboard.class));
                                ((android.app.Activity) context).finish();
                            }
                        }
                    });
            return "ee";


        } catch (Exception e) {
            // Returning the exception in case of failure
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        // Casting the response to a Sring variable
        String response = o.toString();

        // Showing the reponse to the user in a Toast message
        // here we are using the php file response, it will be changed later
        Toast.makeText(context,response,Toast.LENGTH_LONG).show();

    }
}
