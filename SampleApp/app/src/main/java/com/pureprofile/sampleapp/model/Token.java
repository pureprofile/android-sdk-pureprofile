package com.pureprofile.sampleapp.model;

import android.content.Context;
import android.preference.PreferenceManager;

public class Token {
    private static String sToken = null;
    private static final String PREF_TOKEN = "token";

    public static String getToken(Context context) {
        if (sToken == null) {
            sToken = getStoredToken(context);
        }
        return sToken;
    }

    public static void setToken(Context context, String token) {
        sToken = token;
        setStoredToken(context, token);
    }

    private static String getStoredToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_TOKEN, null);
    }

    private static void setStoredToken(Context context, String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_TOKEN, token)
                .apply();
    }
}
