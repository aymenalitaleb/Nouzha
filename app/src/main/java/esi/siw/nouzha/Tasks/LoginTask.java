package esi.siw.nouzha.Tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import esi.siw.nouzha.Dashboard;
import esi.siw.nouzha.R;


public class LoginTask extends AsyncTask{

    public Context context;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagement session;

    StringBuilder sb = new StringBuilder();
    ProgressDialog progressDialog;

    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show(); // Display Progress Dialog
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {

            // Session Manager
            session = new SessionManagement(context.getApplicationContext());
            String email = (String) objects[0];
            final String password = (String) objects[1];

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            if (!task.isSuccessful()) {
                                // there was an error
                                onPostExecute(new String("error"));
                            } else {
                                onPostExecute(new String("success"));
                            }
                        }
                    });

            // Creating user login session
            // For testing i am stroing name, email as follow
            // Use user real data

            return sb.toString();

        } catch (Exception e) {
            return new String(e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
        String response = (String) o;
        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
        if (response.equals("success")) {
            Intent intent = new Intent(context,Dashboard.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Error");
            dialog.setMessage("Username and password doesn't match !");
            dialog.show();
        }
    }


}
