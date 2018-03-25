package esi.siw.nouzha;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Creator on 25/03/2018.
 */

public class RegisterActivity extends AsyncTask {

    private Context context;


    public RegisterActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){
    }
    @Override
    protected Object doInBackground(Object[] arg0) {
        try{
            // These inputs are sent via the post method
            String firstName = (String)arg0[0];
            String lastName = (String)arg0[1];
            String email = (String)arg0[2];
            String password = (String)arg0[3];

            // The file that recieve the post method and read the inputs
            String link="http://nouzha-app.000webhostapp.com/index.php";
            String data  = URLEncoder.encode("firstName", "UTF-8") + "=" +
                    URLEncoder.encode(firstName, "UTF-8");
            data += "&" + URLEncoder.encode("lastName", "UTF-8") + "=" +
                    URLEncoder.encode(lastName, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            Log.i("data: ", data);

            // Create a connection to the file
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

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Reading the response of the server
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            // The response will be read by the onPostExecute method
            return sb.toString();

        } catch(Exception e){
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
