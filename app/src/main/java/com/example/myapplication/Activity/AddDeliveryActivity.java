package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseDelivery;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Models.City;
import com.example.myapplication.Models.Delivery;
import com.example.myapplication.Models.Feeship;
import com.example.myapplication.Models.Pronvice;
import com.example.myapplication.Models.Ward;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddDeliveryActivity extends AppCompatActivity {
    static Spinner spinerCity, spinerPronvice, spinerWard;
    ImageView imgBack;
    Button btnAddAddress;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIMain apiMain;
    List<City> cityList;
    List<Pronvice> pronviceList;
    List<Ward> wardList;
    Context context;
    static String city, province;
    TextView address;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();
    Feeship feeship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);
        context = this;
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        initUi();
        enventClick();
        getData();
    }

    private void enventClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getData() {
        compositeDisposable.add(apiMain.getCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cityAPI -> {
                            cityList = cityAPI.getData();
                            ArrayList<String> cityNames = new ArrayList<>();
                            for (int i = 0; i < cityList.size(); i++) {
                                cityNames.add(cityList.get(i).getName_city());
                            }
                            ArrayAdapter<String> cityArrayAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cityNames);
                            spinerCity.setAdapter(cityArrayAdapter);
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));

        spinerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = spinerCity.getSelectedItem().toString();
                // get province
                compositeDisposable.add(apiMain.getPronvice(city)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                provinceAPI -> {
                                    pronviceList = provinceAPI.getPronviceList();
                                    ArrayList<String> provinceName = new ArrayList<>();
                                    for (int i = 0; i < pronviceList.size(); i++) {
                                        provinceName.add(pronviceList.get(i).getName_quanhuyen());
                                    }
                                    ArrayAdapter<String> cityArrayAdapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, provinceName);
                                    spinerPronvice.setAdapter(cityArrayAdapter);
                                }, throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        ));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinerPronvice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province = spinerPronvice.getSelectedItem().toString();
                compositeDisposable.add(apiMain.getWard(province)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                wardAPI -> {
                                    wardList = wardAPI.getWardList();
                                    ArrayList<String> wardNames = new ArrayList<>();
                                    for (int i = 0; i < wardList.size(); i++) {
                                        wardNames.add(wardList.get(i).getName_xaphuong());
                                    }
                                    ArrayAdapter<String> cityArrayAdapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, wardNames);
                                    spinerWard.setAdapter(cityArrayAdapter);
                                    spinerWard.getSelectedItem().toString();
                                }, throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        ));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Delivery delivery = new Delivery();
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = spinerCity.getSelectedItem().toString();
                String provinceName = spinerPronvice.getSelectedItem().toString();
                String wardName = spinerWard.getSelectedItem().toString();
                String tvaddress = address.getText().toString();
                String address = tvaddress + ", " + wardName + ", " + provinceName + ", " + cityName;
                // get fee ship
                compositeDisposable.add(apiMain.shipping_fee(address)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                feeshipAPI -> {
                                    if (feeshipAPI.getStatus_code() == 200) {
                                        // Có trong bảng tính phí
                                        feeship = feeshipAPI.getData();
                                        delivery.setFee_feeship(feeship.getFee_feeship());
                                        delivery.setCustomer_id(customer_id);
                                        delivery.setAddress(address);
                                        delivery.setStatus_delivery(false);
                                        DatabaseDelivery.getInstance(context).dao().insertDelivery(delivery);
                                        Toast.makeText(getApplicationContext(), "Thêm địa chỉ thành công", Toast.LENGTH_LONG).show();
                                    } else if (feeshipAPI.getStatus_code() == 202) {
                                        // Không có trong bản tính phí
                                        feeship = feeshipAPI.getData();
                                        delivery.setFee_feeship(feeship.getFee_feeship());
                                        delivery.setCustomer_id(customer_id);
                                        delivery.setAddress(address);
                                        delivery.setStatus_delivery(false);
                                        DatabaseDelivery.getInstance(context).dao().insertDelivery(delivery);
                                        Toast.makeText(getApplicationContext(), "Thêm địa chỉ thành công", Toast.LENGTH_LONG).show();
                                    }
                                }, throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        ));
            }
        });
    }

    private void initUi() {
        cityList = new ArrayList<>();
        pronviceList = new ArrayList<>();
        wardList = new ArrayList<>();
        spinerCity = findViewById(R.id.spinerCity);
        spinerPronvice = findViewById(R.id.spinerPronvice);
        spinerWard = findViewById(R.id.spinerWard);
        imgBack = findViewById(R.id.imgBack);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        address = findViewById(R.id.address);
    }
}