package com.android11.netspeed.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharePreferenceUtil(Context context, String file) {
        sp = context.getApplicationContext().getSharedPreferences(file, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setValueString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getValueString(String key) {
        return sp.getString(key, "");
    }

    public void setValueInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }


    public int getValueInt(String key) {
        return sp.getInt(key, -1);
    }


}
