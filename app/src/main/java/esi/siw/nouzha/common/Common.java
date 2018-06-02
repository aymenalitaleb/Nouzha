package esi.siw.nouzha.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import esi.siw.nouzha.models.Request;
import esi.siw.nouzha.models.User;

public class Common {
    public static User currentUser;
    public static Request currentRequest;

    public static final String INTENT_ACITIVTY_ID ="activityId";

    public static final String UPDATE ="Update";
    public static final String DELETE ="Delete";
    public static final String USER_KEY ="User";
    public static final String IS_STAFF_KEY ="true";
    public static final String PWD_KEY ="Password";

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

















