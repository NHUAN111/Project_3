package com.example.myapplication.Activity;

import static com.example.myapplication.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.AdapterCheckout;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseDelivery;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Coupon;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.Models.Delivery;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderActivity extends AppCompatActivity {
    ImageView imgBackOrder;
    Button btnCancel, btnCheckout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIMain apiMain;
    TextView tvPriceTotal, tvNameCheck, tvPhoneCheck, tvAddressCheckOut, tvEmailCheck, tvAddress, tvFeeShip, tvTotal, tvTotalCoupon;
    ListView listView;
    Spinner spinner;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();
    AdapterCheckout adapterCheckout;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_order);
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        initUi();
        getData();
        eventClick();
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(OrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout.layout_dialog_loading);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wLayoutParams = window.getAttributes();
        window.setAttributes(wLayoutParams);
        wLayoutParams.gravity = Gravity.CENTER;

        if (Gravity.CENTER == Gravity.CENTER) {
            dialog.setCancelable(true);
        }
        dialog.show();
    }

    private void eventClick() {
        imgBackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeliveryActivity.class);
                startActivity(intent);
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buider = new AlertDialog.Builder(OrderActivity.this).setTitle("Thông báo")
                        .setMessage("Xác nhận để đặt hàng")
                        .setCancelable(false)
                        .setPositiveButton("Có", (dialogInterface, i) -> {
                            openDialog();
                            order();
                        }).setNegativeButton("Không", null);
                AlertDialog alertDialog = buider.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    private void order() {
        String customer_name = tvNameCheck.getText().toString().trim();
        String customer_phone = tvPhoneCheck.getText().toString().trim();
        String customer_address = tvAddressCheckOut.getText().toString().trim();
        String customer_email = tvEmailCheck.getText().toString().trim();
        String payment_method = spinner.getSelectedItem().toString();

        if (customer_address.isEmpty() || customer_email.isEmpty() || customer_name.isEmpty() || customer_phone == null) {
            btnCheckout.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        } else {
            int id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();
            List<Cart> cartList = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
            Gson gson = new Gson();
            String json = gson.toJson(cartList);
            String coupon_price;
            String order_coupon;
            Coupon coupon = MySharedPreferencesManager.getCoupon(Constant.PREF_KEY_COUPON);
            if (coupon == null) {
                coupon_price = "0";
                order_coupon = "0";
            } else {
                coupon_price = String.valueOf(coupon.getCoupon_number());
                order_coupon = coupon.getCoupon_code();
            }
            int order_feeship = DatabaseDelivery.getInstance(context).dao().getDelivery(customer_id, true).getFee_feeship();
            // Trực tiếp là 1, trực tuyến là 2
            if (payment_method == "Thanh toán tiền mặt") {
                compositeDisposable.add(apiMain.order_detail(id, customer_name, customer_address, customer_phone, customer_email, 1, String.valueOf(coupon_price), order_coupon, String.valueOf(order_feeship), json)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                foodAPI -> {
                                    if (foodAPI.getStatus_code() == 200) {
                                        DatabaseCart.getInstance(getApplicationContext()).Dao().deleteAllCart(customer_id);
                                        MySharedPreferencesManager.putCoupon(Constant.PREF_KEY_COUPON, null);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                    } else if (foodAPI.getStatus_code() == 202) {
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage() + " fail", Toast.LENGTH_SHORT).show();
                                }
                        )
                );
            } else {
                compositeDisposable.add(apiMain.order_detail(id, customer_name, customer_address, customer_phone, customer_email, 2, String.valueOf(coupon_price), order_coupon, String.valueOf(order_feeship), json)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                foodAPI -> {
                                    if (foodAPI.getStatus_code() == 200) {
                                        DatabaseCart.getInstance(getApplicationContext()).Dao().deleteAllCart(customer_id);
                                        MySharedPreferencesManager.putCoupon(Constant.PREF_KEY_COUPON, null);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                    } else if (foodAPI.getStatus_code() == 202) {
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                                }
                        )
                );
            }
        }
    }

    private void getData() {
        Customer customer = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER);
        String name = customer.getCustomer_name();
        String phone = customer.getCustomer_phone();
        String email = customer.getCustomer_email();

        Coupon coupon_code = MySharedPreferencesManager.getCoupon(Constant.PREF_KEY_COUPON);
        Delivery delivery = DatabaseDelivery.getInstance(getApplicationContext()).dao().getDelivery(customer_id, true);
        if (coupon_code == null) {
            tvTotalCoupon.setText("Tổng (Không có mã giảm)");
        } else {
            String coupon_name = coupon_code.getCoupon_code();
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            if (coupon_code.getCoupon_condition() == 1) {
                tvTotalCoupon.setText("Tổng (" + coupon_name + ") - " + coupon_code.getCoupon_number() + " %");
            } else if (coupon_code.getCoupon_condition() == 2) {
                tvTotalCoupon.setText("Tổng (" + coupon_name + ") - " + decimalFormat.format(Double.parseDouble(coupon_code.getCoupon_number() + "")) + " đ");
            }
        }
        if (delivery == null && customer != null) {
            tvNameCheck.setText(name);
            tvPhoneCheck.setText(phone);
            tvEmailCheck.setText(email);
            tvAddressCheckOut.setText("");
            tvFeeShip.setText("0 đ");
        } else if (delivery != null && customer != null) {
            String address = delivery.getAddress();
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvNameCheck.setText(name);
            tvPhoneCheck.setText(phone);
            tvEmailCheck.setText(email);
            tvAddressCheckOut.setText(address);
            tvFeeShip.setText(decimalFormat.format(Double.parseDouble(delivery.getFee_feeship() + "")) + " đ");
        }

        String[] options = new String[]{"Thanh toán tiền mặt", "Thanh toán trực tuyến"};
        ArrayAdapter<String> optionsArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options);
        spinner.setAdapter(optionsArrayAdapter);

        List<Cart> data = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
        int total = 0;
        Coupon coupon = MySharedPreferencesManager.getCoupon(Constant.PREF_KEY_COUPON);
        for (int i = 0; i < data.size(); i++) {
            long price = data.get(i).getPriceFood();
            int qty = data.get(i).getQtyFood();
            long subtotal = Long.parseLong(price * qty + "");
            total += subtotal;
        }
        if (delivery == null) {
            if (coupon == null) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                tvPriceTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                tvTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
            } else {
                // 1: %, 2: đ
                if (coupon.getCoupon_condition() == 1) {
                    int total_coupon = (total * coupon.getCoupon_number()) / 100;
                    total = (total - total_coupon);
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    tvPriceTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                    tvTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                } else if (coupon.getCoupon_condition() == 2) {
                    int total_coupon = total - coupon.getCoupon_number();
                    total = total_coupon;
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    tvPriceTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                    tvTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                }
                adapterCheckout = new AdapterCheckout(getApplicationContext(), data);
                listView.setAdapter(adapterCheckout);
            }
        } else {
            int fee_feeship = delivery.getFee_feeship();
            if (coupon == null) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                tvPriceTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                tvTotal.setText(decimalFormat.format(Double.parseDouble(total + fee_feeship + "")) + " đ");
            } else {
                // 1: %, 2: đ
                if (coupon.getCoupon_condition() == 1) {
                    int total_coupon = (total * coupon.getCoupon_number()) / 100;
                    total = (total - total_coupon);
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    tvPriceTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                    tvTotal.setText(decimalFormat.format(Double.parseDouble(total + fee_feeship + "")) + " đ");
                } else if (coupon.getCoupon_condition() == 2) {
                    int total_coupon = total - coupon.getCoupon_number();
                    total = total_coupon;
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    tvPriceTotal.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                    tvTotal.setText(decimalFormat.format(Double.parseDouble(total + fee_feeship + "")) + " đ");
                }
                adapterCheckout = new AdapterCheckout(getApplicationContext(), data);
                listView.setAdapter(adapterCheckout);
            }
        }

    }

    private void initUi() {
        imgBackOrder = findViewById(R.id.imgBackOrder);
        spinner = findViewById(R.id.spinerCheckout);
        tvNameCheck = findViewById(R.id.tvNameCheckout);
        tvAddressCheckOut = findViewById(id.tvAddressCheckOut);
        tvEmailCheck = findViewById(id.tvEmailCheck);
        tvPhoneCheck = findViewById(id.tvPhoneCheckOut);
        tvPriceTotal = findViewById(R.id.tvPriceTotal);
        listView = findViewById(R.id.listAllFoodCheckout);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnCancel = findViewById(R.id.btnCancel);
        tvAddress = findViewById(R.id.tvAddress);
        tvFeeShip = findViewById(id.tvFeeShip);
        tvTotal = findViewById(id.tvTotal);
        tvTotalCoupon = findViewById(R.id.tvTotalCoupon);

        List<Cart> data = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
        adapterCheckout = new AdapterCheckout(getApplicationContext(), data);
        listView.setAdapter(adapterCheckout);
    }

    private void delivery() {
        Delivery delivery = DatabaseDelivery.getInstance(getApplicationContext()).dao().getDelivery(customer_id, true);
        if (delivery == null) {
            tvAddressCheckOut.setText("");
            tvFeeShip.setText("0 đ");
        } else {
            String address = delivery.getAddress();
            tvAddressCheckOut.setText(address);
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvFeeShip.setText(decimalFormat.format(Double.parseDouble(delivery.getFee_feeship() + "")) + " đ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        delivery();
    }
}