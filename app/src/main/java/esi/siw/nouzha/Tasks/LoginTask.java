package esi.siw.nouzha.Tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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
            String username = (String) objects[0];
            String password = (String) objects[1];


            // For testing puspose username, password is checked with sample data
            // username = test
            // password = test

            String link = "http://nouzha-app.000webhostapp.com/login.php" ;
            String data  = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            // Sending the data to the server
            wr.write( data );
            wr.flush();

            // Create a BufferReader to read the response from the server
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            String line = null;

            // Reading the response of the server
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            // Creating user login session
            // For testing i am stroing name, email as follow
            // Use user real data

            return sb.toString();

        } catch (UnsupportedEncodingException e) {
            return new String(e.getMessage());
        } catch (MalformedURLException e) {
            return new String(e.getMessage());
        } catch (IOException e) {
            return new String(e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
        String response = (String) o;
        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String queryResult = jsonObject.getString("query_result");
                if (queryResult.equals("SUCCESS")) {
                    int idUser = jsonObject.getInt("idUser");
                    String FirstName = jsonObject.getString("FirstName");
                    String LastName = jsonObject.getString("LastName");
                    String Birthday = jsonObject.getString("Birthday");
                    String Birthplace = jsonObject.getString("Birthplace");
                    String Avatar = jsonObject.getString("Avatar");
                    String Email = jsonObject.getString("Email");
                    String Profession = jsonObject.getString("Profession");
                    session.createLoginSession(idUser,FirstName,LastName,Birthday,Birthplace,Email,Profession,Avatar);
                    Intent intent = new Intent(context,Dashboard.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Error");
                    dialog.setMessage("Username and password doesn't match !");
                    dialog.show();
                }
            } catch (JSONException e) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Error");
                dialog.setMessage(e.getMessage());
                dialog.show();

            }
        }
    }

}
