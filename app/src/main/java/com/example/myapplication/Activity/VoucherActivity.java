package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.Adapter.AdapterCoupon;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Interface.IclickCoupon;
import com.example.myapplication.Models.Coupon;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VoucherActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recyclerView;
    APIMain apiMain;
    List<Coupon> couponList;
    AdapterCoupon adapterCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        initUi();
        getData();
    }

    private void getData() {
        // Get Coupon
        compositeDisposable.add(apiMain.getCoupon()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        couponAPI -> {
                            couponList = couponAPI.getData();
                            adapterCoupon = new AdapterCoupon(getApplicationContext(), couponList, Constant.VIEW_TYPE_ICON_COUPON_COUPON,new IclickCoupon() {
                                @Override
                                public void iclickCoupon(Coupon coupon) {
                                    compositeDisposable.add(apiMain.check_coupon(coupon.getCoupon_code())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    couponAPI -> {
                                                        if (couponAPI.getStatus_code() == 200){
                                                            Coupon coupon_api = new Coupon(couponAPI.getData().get(0).getCoupon_code(),
                                                                    couponAPI.getData().get(0).getCoupon_number(),
                                                                    couponAPI.getData().get(0).getCoupon_condition());
                                                            Toast.makeText(getApplicationContext(), "Thêm mã giảm thành công", Toast.LENGTH_LONG).show();
                                                            MySharedPreferencesManager.putCoupon(Constant.PREF_KEY_COUPON, coupon_api);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Mã giảm hết hiệu lực", Toast.LENGTH_LONG).show();
                                                            finish();
                                                        }
                                                    },
                                                    throwable -> {
                                                        Toast.makeText(getApplicationContext(), "Loi Ket Noi Database", Toast.LENGTH_LONG).show();
                                                    }
                                            )
                                    );
                                }
                            });
                            recyclerView.setAdapter(adapterCoupon);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Loi Ket Noi Database", Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void initUi() {
        recyclerView = findViewById(R.id.recVoucher);
        adapterCoupon = new AdapterCoupon(getApplicationContext(), couponList, Constant.VIEW_TYPE_ICON_COUPON_COUPON);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}