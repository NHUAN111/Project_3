package com.example.myapplication.DatabaseLocal.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.Constant.Constant;

public class MySharedPreferences {
    private static final String MY_SHARE_PREFERENCES = Constant.MY_SHARE_PREFERENCES;
    private static Context mContext;

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    public void putBoolean(String key, Boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key , value);
        editor.apply();
    }

    public boolean getBoolean(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key , false);
    }

    public void putString(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key , value);
        editor.apply();
    }
    public String getString(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key , null);
    }

}
