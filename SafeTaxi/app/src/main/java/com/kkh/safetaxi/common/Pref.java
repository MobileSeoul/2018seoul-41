package com.kkh.safetaxi.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2018-08-14.
 */

public class Pref {
    public static boolean setPreferenceString(String key, String val, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, val);
        return edit.commit();
    }

    public static String getPreferenceString(String key, Context context, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key, defaultValue);
    }
}
