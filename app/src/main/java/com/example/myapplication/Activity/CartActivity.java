package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Adapter.AdapterCart;
import com.example.myapplication.Adapter.AdapterCoupon;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.Interface.IclickCart;
import com.example.myapplication.Interface.IclickCoupon;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Coupon;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    static TextView tvTotalFood, tvCart, tvVoucher, tvQtyCart;
    Button btnCheckOut;
    RecyclerView recCartFood;
    AdapterCart adapterCart;
    ImageView imgBackCart;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout layoutImportVoucher;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        anhXa();
        initControl();
        eventClick();
        reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaCart();
    }

    private void reload() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaCart();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void eventClick() {
        imgBackCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<Cart> cart = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
        if (cart.size() > 0) {
            btnCheckOut.setEnabled(true);
            btnCheckOut.setBackgroundColor(Color.RED);
            layoutImportVoucher.setEnabled(true);
            btnCheckOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            layoutImportVoucher.setEnabled(false);
            btnCheckOut.setEnabled(false);
            btnCheckOut.setBackgroundColor(Color.GRAY);
        }

        Coupon coupon = MySharedPreferencesManager.getCoupon(Constant.PREF_KEY_COUPON);
        if (coupon == null) {
            layoutImportVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), VoucherActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            layoutImportVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MySharedPreferencesManager.putCoupon(Constant.PREF_KEY_COUPON, null);
                    Intent intent = new Intent(getApplicationContext(), VoucherActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void initControl() {
        List<Cart> data = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
        Coupon coupon = MySharedPreferencesManager.getCoupon(Constant.PREF_KEY_COUPON);
        if (data.size() == 0) {
            tvCart.setText("Giỏ hàng trống");
            tvQtyCart.setText("Giỏ hàng (0)");
        } else {
            tvQtyCart.setText("Giỏ hàng ("+data.size()+")");
            int total = 0;
            for (int i = 0; i < data.size(); i++) {
                long price = data.get(i).getPriceFood();
                int qty = data.get(i).getQtyFood();
                long subtotal = Long.parseLong(price * qty + "");
                total += subtotal;
            }
            if (coupon == null) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                tvTotalFood.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                tvVoucher.setText("Chọn hoặc nhập mã >");
            } else {
                // 1: %, 2: đ
                if (coupon.getCoupon_condition() == 1) {
                    int total_coupon = (total * coupon.getCoupon_number()) / 100;
                    total = total - total_coupon;
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    tvTotalFood.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                } else if (coupon.getCoupon_condition() == 2) {
                    int total_coupon = total - coupon.getCoupon_number();
                    total = total_coupon;
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    tvTotalFood.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                }
                tvVoucher.setText(coupon.getCoupon_code());
            }
        }
        adapterCart = new AdapterCart(this, data, new IclickCart() {
            @Override
            public void deleteCart(Cart cart) {
                AlertDialog.Builder buider = new AlertDialog.Builder(CartActivity.this).setTitle("Thông báo")
                        .setMessage("Bạn muốn xóa sản phẩm")
                        .setPositiveButton("Có", (dialogInterface, i) -> {
                            deleteCartV2(cart);
                            Toast.makeText(CartActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            initControl();
                        }).setNegativeButton("Hủy", null);
                AlertDialog alertDialog = buider.create();
                alertDialog.show();
            }

            @Override
            public void plusCart(Cart cart) {
                plusCartV2(cart);
            }

            @Override
            public void minusCart(Cart cart) {
                minusCartV2(cart);
            }
        });
        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(this);
        recCartFood.setLayoutManager(layoutManagerFood);
        recCartFood.setHasFixedSize(true);
        recCartFood.setAdapter(adapterCart);
    }


    private void deleteCartV2(Cart cart) {
        DatabaseCart.getInstance(getApplicationContext()).Dao().delete(cart);
        List<Cart> data = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
        if (data.size() == 0) {
            MySharedPreferencesManager.putCoupon(Constant.PREF_KEY_COUPON, null);
        }
        loaCart();
    }

    private void plusCartV2(Cart cart) {
        int qty = cart.getQtyFood() + 1;
        cart.setQtyFood(qty);
        DatabaseCart.getInstance(getApplicationContext()).Dao().update(cart);
        loaCart();
    }

    private void minusCartV2(Cart cart) {
        cart.setQtyFood(cart.getQtyFood() - 1);
        if (cart.getQtyFood() <= 1) {
            cart.setQtyFood(1);
        }
        DatabaseCart.getInstance(getApplicationContext()).Dao().update(cart);
        loaCart();
    }

    private void anhXa() {
        List<Cart> data = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
        tvTotalFood = findViewById(R.id.tvTotalFood);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        recCartFood = findViewById(R.id.recCartFood);
        imgBackCart = findViewById(R.id.imgBackCart);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvCart = findViewById(R.id.tvCart);
        layoutImportVoucher = findViewById(R.id.layoutImportVoucher);
        tvVoucher = findViewById(R.id.tvVoucher);
        tvQtyCart = findViewById(R.id.tvQtyCart);

        adapterCart = new AdapterCart(this, data);
        recCartFood.setAdapter(adapterCart);
    }

    private void loaCart() {
        Coupon coupon = MySharedPreferencesManager.getCoupon(Constant.PREF_KEY_COUPON);
        List<Cart> data = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
        if (coupon == null) {
            tvVoucher.setText("Chọn hoặc nhập mã >");
        } else {
            tvVoucher.setText(coupon.getCoupon_code());
        }
        int total = 0;
        if (data.size() == 0) {
            btnCheckOut.setEnabled(false);
            btnCheckOut.setBackgroundColor(Color.GRAY);
            tvCart.setText("Giỏ hàng trống");
            tvTotalFood.setText("0 đ");
            tvQtyCart.setText("Giỏ hàng (0)");
        } else if (data.size() > 0) {
            tvQtyCart.setText("Giỏ hàng ("+data.size()+")");
            btnCheckOut.setEnabled(true);
            btnCheckOut.setBackgroundColor(Color.RED);
            for (int i = 0; i < data.size(); i++) {
                long price = data.get(i).getPriceFood();
                int qty = data.get(i).getQtyFood();
                long subtotal = Long.parseLong(price * qty + "");
                total += subtotal;
            }
            if (coupon == null) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                tvTotalFood.setText(decimalFormat.format(Double.parseDouble(total + "")) + " đ");
                tvVoucher.setText("Chọn hoặc nhập mã >");
            } else {
                // 1: %, 2: đ
                if (coupon.getCoupon_condition() == 1) {
                    int total_coupon = (total * coupon.getCoupon_number()) / 100;
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    tvTotalFood.setText(decimalFormat.format(Double.parseDouble(total - total_coupon + "")) + " đ");
                } else if (coupon.getCoupon_condition() == 2) {
                    int total_coupon = total - coupon.getCoupon_number();
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    tvTotalFood.setText(decimalFormat.format(Double.parseDouble(total_coupon + "")) + " đ");
                }
            }
        }

        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(this);
        recCartFood.setLayoutManager(layoutManagerFood);
        recCartFood.setHasFixedSize(true);
        recCartFood.setAdapter(adapterCart);
    }
}