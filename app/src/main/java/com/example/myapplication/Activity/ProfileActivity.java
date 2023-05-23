package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.R;

public class ProfileActivity extends AppCompatActivity {
    TextView customer_name, customer_phone, customer_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initUi();
        getData();
    }

    private void getData() {
        Customer customer = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER);
        customer_name.setText(customer.getCustomer_name());
        customer_phone.setText(customer.getCustomer_phone());
        customer_email.setText(customer.getCustomer_email());
    }

    private void initUi() {
        customer_name = findViewById(R.id.customer_name);
        customer_phone = findViewById(R.id.customer_phone);
        customer_email = findViewById(R.id.customer_email);
    }

}