package esi.siw.nouzha.Tasks;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import esi.siw.nouzha.Dashboard;
import esi.siw.nouzha.MainActivity;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Auth";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_ID = "idUser";
    public static final String KEY_FIRSTNAME = "FirstName";
    public static final String KEY_LASTNAME = "LastName";
    public static final String KEY_BIRTHDAY = "Birthday";
    public static final String KEY_BIRTHPLACE = "Birthplace";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_PROFESSION = "Profession";
    public static final String KEY_AVATAR = "Avatar";


    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(int id, String FirstName,String LastName, String Birthday, String Birthplace, String Email, String Profession, String Avatar){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_FIRSTNAME, FirstName);
        editor.putString(KEY_LASTNAME, LastName);
        editor.putString(KEY_BIRTHDAY, Birthday);
        editor.putString(KEY_BIRTHPLACE, Birthplace);
        editor.putString(KEY_EMAIL, Email);
        editor.putString(KEY_PROFESSION, Profession);
        editor.putString(KEY_AVATAR, Avatar);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Dashboard.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
            ((Activity) _context).finish();
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // idUser
        user.put(KEY_ID, String.valueOf(pref.getInt(KEY_ID, 0)));
        user.put(KEY_FIRSTNAME, pref.getString(KEY_FIRSTNAME, ""));
        user.put(KEY_LASTNAME, pref.getString(KEY_LASTNAME, ""));
        user.put(KEY_BIRTHPLACE, pref.getString(KEY_BIRTHPLACE, ""));
        user.put(KEY_BIRTHDAY, pref.getString(KEY_BIRTHDAY, ""));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_PROFESSION, pref.getString(KEY_PROFESSION, ""));
        user.put(KEY_AVATAR, pref.getString(KEY_AVATAR, ""));


        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
