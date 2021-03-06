package esi.siw.nouzha.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import esi.siw.nouzha.models.User;

public class CommonStaff {

    public static User currentUser;

    public static final String UPDATE ="Update";
    public static final String DELETE ="Delete";



    public static final int PICK_IMAGE_REQUEST = 71;

    public static boolean isConnectedToInternet (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i=0;i<info.length;i++) {
                    if (info[0].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }

        }
        return false;
    };




}
