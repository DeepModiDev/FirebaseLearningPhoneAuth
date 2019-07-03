package com.deepmodi.firebaselearningphoneauth.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {

    public static boolean InternetCheckSystem(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for(int i=0; i<info.length ; i++)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static final String USER_PHONE_NUMBER = "PhoneNumber";
    public static final String USER_PASSWORD = "UserPassword";
    public static final String CURRENT_USER = "User";

}


