package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseDelivery;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.R;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    int SPLASH_TIME_OUT = 2000;
    Context context;
    MySharedPreferencesManager mySharedPreferencesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        Customer customer = mySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER);
//        DatabaseCart.getInstance(getApplicationContext()).Dao().deleteAllCart();
//        DatabaseDelivery.getInstance(context).dao().deleteAllDelivery(customer.getCustomer_id());

        if (customer == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginOrRegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

    }
}