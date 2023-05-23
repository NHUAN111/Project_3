package com.example.myapplication.DatabaseLocal.SharedPreferences;

import android.content.Context;

import com.example.myapplication.Models.Coupon;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.Models.Delivery;
import com.example.myapplication.Models.Feeship;
import com.google.gson.Gson;

public class MySharedPreferencesManager {
    private static MySharedPreferencesManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new MySharedPreferencesManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static MySharedPreferencesManager getInstance(){
        if (instance == null){
            instance = new MySharedPreferencesManager();
        }
        return instance;
    }

    public static void putFirstInstallApp(String key, boolean value){
        MySharedPreferencesManager.getInstance().mySharedPreferences.putBoolean(key , value);
    }

    public static boolean getFirstInstallApp(String key){
       return MySharedPreferencesManager.getInstance().mySharedPreferences.getBoolean(key);
    }

    public static void putCustomer(String key, Customer customer){
        Gson gson = new Gson();
        String strJsonCustomer = gson.toJson(customer);
        MySharedPreferencesManager.getInstance().mySharedPreferences.putString(key , strJsonCustomer);
    }

    public static Customer getCustomer(String key){
        String strJsonCustomer = MySharedPreferencesManager.getInstance().mySharedPreferences.getString(key);
        Gson gson = new Gson();
        Customer customer = gson.fromJson(strJsonCustomer , Customer.class);
        return customer;
    }

//    public static void putDelivery(String key, Delivery value){
//        Gson gson = new Gson();
//        String strJsonDelivery = gson.toJson(value);
//        MySharedPreferencesManager.getInstance().mySharedPreferences.putString(key , strJsonDelivery);
//    }
//
//    public static Delivery getDelivery(String key){
//        String strJsonDelivery = MySharedPreferencesManager.getInstance().mySharedPreferences.getString(key);
//        Gson gson = new Gson();
//        Delivery delivery = gson.fromJson(strJsonDelivery , Delivery.class);
//        return delivery;
//    }

    public static void putCoupon(String key, Coupon value){
        Gson gson = new Gson();
        String strJsonCoupon = gson.toJson(value);
        MySharedPreferencesManager.getInstance().mySharedPreferences.putString(key , strJsonCoupon);
    }

    public static Coupon getCoupon(String key){
        String strJsonCoupon = MySharedPreferencesManager.getInstance().mySharedPreferences.getString(key);
        Gson gson = new Gson();
        Coupon coupon = gson.fromJson(strJsonCoupon , Coupon.class);
        return coupon;
    }

    public static void putFeeship(String key, Feeship value){
        Gson gson = new Gson();
        String strJsonFeeship = gson.toJson(value);
        MySharedPreferencesManager.getInstance().mySharedPreferences.putString(key , strJsonFeeship);
    }

    public static Feeship getFeeship(String key){
        String strJsonFeeship = MySharedPreferencesManager.getInstance().mySharedPreferences.getString(key);
        Gson gson = new Gson();
        Feeship feeship = gson.fromJson(strJsonFeeship , Feeship.class);
        return feeship;
    }

}
