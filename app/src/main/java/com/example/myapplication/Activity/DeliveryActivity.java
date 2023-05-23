package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.AdapterDelivery;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseDelivery;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DeliveryDao;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Interface.ItemClickDelivery;
import com.example.myapplication.Models.City;
import com.example.myapplication.Models.Delivery;
import com.example.myapplication.Models.Feeship;
import com.example.myapplication.Models.Pronvice;
import com.example.myapplication.Models.Shipping;
import com.example.myapplication.Models.Ward;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DeliveryActivity extends AppCompatActivity {
    ImageView imgBack;
    Button btnAddAddress;
    Context context;
    RecyclerView recAllDelivery;
    AdapterDelivery adapterDelivery;
    TextView tvNull;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        context = this;
        initUi();
        getData();
        enventClick();
    }

    private void enventClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddDeliveryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void deletesDelivery(Delivery delivery) {
        DatabaseDelivery.getInstance(context).dao().deleteDelivery(delivery);
        loadData();
    }

    private List<Delivery> checkedDeliveries = new ArrayList<>();
    private void checkedDelivery(Delivery delivery) {
        // Clear the list of checked deliveries
        checkedDeliveries.clear();

        // Get the existing checked delivery, if any
        Delivery existingDelivery = DatabaseDelivery.getInstance(context).dao().searchDelivery(customer_id, true);
        if (existingDelivery != null) {
            // Uncheck the existing delivery
            existingDelivery.setStatus_delivery(false);
            DatabaseDelivery.getInstance(context).dao().updateDelivery(existingDelivery);
        }

        // Check the new delivery
        delivery.setStatus_delivery(true);
        DatabaseDelivery.getInstance(context).dao().updateDelivery(delivery);

        // Add the new delivery to the list of checked deliveries
        checkedDeliveries.add(delivery);
        loadData();
    }

    private void uncheckDelivery(Delivery delivery) {
        if (!delivery.isStatus_delivery()) {
            return;
        }

        // Uncheck the delivery
        delivery.setStatus_delivery(false);
        DatabaseDelivery.getInstance(context).dao().updateDelivery(delivery);

        // Remove the delivery from the list of checked deliveries
        checkedDeliveries.remove(delivery);

        // If there are any deliveries left in the list, check the first one
        if (!checkedDeliveries.isEmpty()) {
            Delivery firstDelivery = checkedDeliveries.get(0);
            firstDelivery.setStatus_delivery(true);
            DatabaseDelivery.getInstance(context).dao().updateDelivery(firstDelivery);
        }
        loadData();
    }


    private void initUi() {
        List<Delivery> deliveryList = DatabaseDelivery.getInstance(context).dao().getListDelivery(customer_id);
        imgBack = findViewById(R.id.imgBack);
        recAllDelivery = findViewById(R.id.recAllDelivery);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        tvNull = findViewById(R.id.tvNullDelivery);

        adapterDelivery = new AdapterDelivery(this, deliveryList);
        recAllDelivery.setAdapter(adapterDelivery);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void getData() {
        List<Delivery> deliveryList = DatabaseDelivery.getInstance(context).dao().getListDelivery(customer_id);
        if (deliveryList.size() == 0) {
            tvNull.setText("Dữ liệu trống");
        } else {
            tvNull.setText("");
        }
        adapterDelivery = new AdapterDelivery(this, deliveryList, new ItemClickDelivery() {
            @Override
            public void deleteDelivery(Delivery delivery) {
                AlertDialog.Builder buider = new AlertDialog.Builder(DeliveryActivity.this).setTitle("Thông báo")
                        .setMessage("Bạn muốn xóa địa chỉ này không ?")
                        .setPositiveButton("Có", (dialogInterface, i) -> {
                            deletesDelivery(delivery);
                            Toast.makeText(DeliveryActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            loadData();
                        }).setNegativeButton("Hủy", null);
                AlertDialog alertDialog = buider.create();
                alertDialog.show();
            }

            @Override
            public void checkDelivery(Delivery delivery) {
                if (delivery.isStatus_delivery() == true) {
                    uncheckDelivery(delivery);
                } else {
                    checkedDelivery(delivery);
                }
            }
        });
        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(this);
        recAllDelivery.setLayoutManager(layoutManagerFood);
        recAllDelivery.setHasFixedSize(true);
        recAllDelivery.setAdapter(adapterDelivery);

    }

    private void loadData() {
        List<Delivery> deliveryList = DatabaseDelivery.getInstance(context).dao().getListDelivery(customer_id);
        if (deliveryList.size() == 0) {
            tvNull.setText("Dữ liệu trống");
        } else {
            tvNull.setText("");
        }
        adapterDelivery = new AdapterDelivery(this, deliveryList, new ItemClickDelivery() {
            @Override
            public void deleteDelivery(Delivery delivery) {
                AlertDialog.Builder buider = new AlertDialog.Builder(DeliveryActivity.this).setTitle("Thông báo")
                        .setMessage("Bạn muốn xóa địa chỉ này không ?")
                        .setPositiveButton("Có", (dialogInterface, i) -> {
                            deletesDelivery(delivery);
                            Toast.makeText(DeliveryActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            loadData();
                        }).setNegativeButton("Hủy", null);
                AlertDialog alertDialog = buider.create();
                alertDialog.show();
            }

            @Override
            public void checkDelivery(Delivery delivery) {
                if (delivery.isStatus_delivery() == true) {
                    uncheckDelivery(delivery);
                } else {
                    checkedDelivery(delivery);
                }
            }
        });
        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(this);
        recAllDelivery.setLayoutManager(layoutManagerFood);
        recAllDelivery.setHasFixedSize(true);
        recAllDelivery.setAdapter(adapterDelivery);
    }

}