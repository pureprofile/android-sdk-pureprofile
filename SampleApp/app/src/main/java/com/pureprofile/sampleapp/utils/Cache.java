package com.pureprofile.sampleapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class Cache {

    private static final String PANEL_CODE = "panelCode";

    public static void setStoredPanelKey(Context context, String key) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PANEL_CODE, key)
                .apply();
    }

    public static String getStoredPanelKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PANEL_CODE, null);
    }
}
