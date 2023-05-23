package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.AdapterInfoOrderDetail;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Models.OrderDetails;
import com.example.myapplication.Models.Orders;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InfoOrderDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView imgBack;
    AdapterInfoOrderDetail adapterInfoOrderDetail;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIMain apiMain;
    List<OrderDetails> orderDetailsList;
    TextView tvPriceTotal, tvFeeShip, tvTotal, tvTotalLeft, tvOrderCode, tvOrderDate, tvCustomerName, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_order_detail);
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        initUi();
        getData();
        eventClick();
    }

    private void eventClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        String order_code = getIntent().getStringExtra("order_code");
        compositeDisposable.add(apiMain.detail_order(order_code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        foodAPI -> {
                            int subtotal = 0;
                            if (foodAPI.getStatus_code() == 200) {
                                orderDetailsList = foodAPI.getData();
                                for (int i = 0; i < orderDetailsList.size(); i++) {
                                    int total_price = orderDetailsList.get(i).getTotal_price();
                                    subtotal += total_price;
                                }
                                adapterInfoOrderDetail = new AdapterInfoOrderDetail(getApplicationContext(), orderDetailsList);
                                recyclerView.setAdapter(adapterInfoOrderDetail);

                                if (orderDetailsList.get(0).getOrder().getCoupon_price().equals("0")) {
                                    tvTotalLeft.setText("Tổng (Không có mã giảm)");
                                } else {
                                    tvTotalLeft.setText("Tổng (Mã giảm " + orderDetailsList.get(0).getOrder().getOrder_coupon() + ")");
                                }

                                // Information order customer
                                tvOrderCode.setText(order_code);
                                tvOrderDate.setText("Ngày đặt: "+orderDetailsList.get(0).getOrder().getCreated_at());
                                tvCustomerName.setText("Người đặt: "+orderDetailsList.get(0).getData_shipping().getShipping_name());
                                tvAddress.setText("Địa chỉ: "+orderDetailsList.get(0).getData_shipping().getShipping_address());

                                int fee_ship = Integer.parseInt(orderDetailsList.get(0).getOrder().getOrder_feeship());
                                DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
                                tvFeeShip.setText(decimal_Format.format(Double.parseDouble(String.valueOf(fee_ship))) + " đ");
                                tvPriceTotal.setText(decimal_Format.format(Double.parseDouble(String.valueOf(subtotal))) + " đ");
                                int coupon_price = Integer.parseInt(orderDetailsList.get(0).getOrder().getCoupon_price());
                                long total = (subtotal - coupon_price) + fee_ship;
                                tvTotal.setText(decimal_Format.format(Double.parseDouble(String.valueOf(total))) + " đ");
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void initUi() {
        orderDetailsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recOrder);
        imgBack = findViewById(R.id.imgBack);
        tvPriceTotal = findViewById(R.id.tvPriceTotal);
        tvFeeShip = findViewById(R.id.tvFeeShip);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotalLeft = findViewById(R.id.tvTotalLeft);
        tvAddress = findViewById(R.id.tvAddress);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderCode = findViewById(R.id.tvOrderCodes);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}