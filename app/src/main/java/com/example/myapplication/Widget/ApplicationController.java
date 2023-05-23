package com.example.myapplication.Widget;

import android.app.Application;

import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;

public class ApplicationController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MySharedPreferencesManager.init(getApplicationContext());
    }
}
