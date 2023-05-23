package com.example.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.AdapterSearch;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseFavourite;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Interface.IclickFood;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Favourite;
import com.example.myapplication.Models.Food;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIMain apiMain;
    List<Food> foodList;
    RecyclerView recyclerView;
    AdapterSearch adapterSearch;
    IclickFood iclickFood;

    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        initUi();
        getData();
        eventClick();
    }

    private void eventClick() {
        // Dialog Food
        iclickFood = new IclickFood() {
            @Override
            public void addFood() {
                openDialog(Gravity.BOTTOM);
            }
        };

        // Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterSearch.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterSearch.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void openDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_detail);
        Window window = dialog.getWindow();

        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wLayoutParams = window.getAttributes();
        wLayoutParams.gravity = gravity;
        window.setAttributes(wLayoutParams);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        }
        TextView tvPlusFood = dialog.findViewById(R.id.tvPlusFood);
        TextView tvMinus = dialog.findViewById(R.id.tvMinusFood);
        TextView tvQtyFood = dialog.findViewById(R.id.tvQtyFood);
        ImageView imgFood = dialog.findViewById(R.id.imgFood);
        ImageView imgBack = dialog.findViewById(R.id.imgBack);
        TextView tvNameFood = dialog.findViewById(R.id.tvNameFoodDialog);
        TextView tvPriceFood = dialog.findViewById(R.id.tvPriceFood);
        Button btnAddFood = dialog.findViewById(R.id.btnAddFood);
        ImageView imgDetailFood = dialog.findViewById(R.id.imgDetailFood);
        ImageView imgLove = dialog.findViewById(R.id.imgLove);

        if (Utils.food != null) {
            tvNameFood.setText(Utils.food.getFood_name());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvPriceFood.setText(decimalFormat.format(Double.parseDouble(Utils.food.getFood_price())) + "đ");
            Glide.with(this).load(Utils.food.getFood_img()).into(imgFood);
            btnAddFood.setText("Thêm +" + decimalFormat.format(Double.parseDouble(Utils.food.getFood_price())) + "đ");
        }

        Cart data = DatabaseCart.getInstance(getApplicationContext()).Dao().searchCart(tvNameFood.getText().toString(), customer_id);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data == null) {
                    long price = Long.parseLong(Utils.food.getFood_price());
                    Cart cart = new Cart();
                    cart.setPriceFood(price);
                    cart.setCustomer_id(customer_id);
                    cart.setQtyFood(Integer.parseInt(tvQtyFood.getText().toString()));
                    cart.setNameFood(Utils.food.getFood_name());
                    cart.setIdFood(Utils.food.getFood_id());
                    cart.setImgFood(Utils.food.getFood_img());
                    DatabaseCart.getInstance(getApplicationContext()).Dao().insert(cart);
                    Toast.makeText(getApplicationContext(), "Thêm Sản Phẩm Thành Công", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    data.setPriceFood(data.getPriceFood());
                    int qty = Integer.parseInt(tvQtyFood.getText().toString());
                    data.setQtyFood(Integer.parseInt(data.getQtyFood() + "") + qty);
                    DatabaseCart.getInstance(getApplicationContext()).Dao().update(data);
                    Toast.makeText(getApplicationContext(), "Thêm Số Lượng Thành Công", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

        tvPlusFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(tvQtyFood.getText().toString());
                if (i < 10) {
                    i++;
                    tvQtyFood.setText(String.valueOf(i));
                    long price = Long.parseLong(Utils.food.getFood_price());
                    int qty = Integer.parseInt(tvQtyFood.getText().toString());
                    double total = price * qty;

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    btnAddFood.setText("Thêm +" + decimalFormat.format(Double.parseDouble(total + "")) + "đ");
                } else if (i > 10) {
                    tvQtyFood.setText(String.valueOf(10));
                    long price = Long.parseLong(Utils.food.getFood_price());
                    int qty = Integer.parseInt(tvQtyFood.getText().toString());
                    double total = price * qty;

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    btnAddFood.setText("Thêm +" + decimalFormat.format(Double.parseDouble(total + "")) + "đ");
                }
            }
        });

        tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(tvQtyFood.getText().toString());
                if (i > 1) {
                    i--;
                    tvQtyFood.setText(String.valueOf(i));
                    long price = Long.parseLong(Utils.food.getFood_price());
                    int qty = Integer.parseInt(tvQtyFood.getText().toString());
                    double total = price * qty;

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    btnAddFood.setText("Thêm +" + decimalFormat.format(Double.parseDouble(total + "")) + "đ");
                } else if (i == 1) {
                    tvQtyFood.setText(String.valueOf(1));
                    long price = Long.parseLong(Utils.food.getFood_price());
                    int qty = Integer.parseInt(tvQtyFood.getText().toString());
                    double total = price * qty;

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    btnAddFood.setText("Thêm +" + decimalFormat.format(Double.parseDouble(total + "")) + "đ");
                }
            }
        });

        imgDetailFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("detail", Utils.food);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Favourite favourite1 = DatabaseFavourite.getInstance(getApplicationContext()).dao().getFavorite(Utils.food.getFood_name(), customer_id);
        if (favourite1 == null) {
            imgLove.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_IN);
            imgLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgLove.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.main), PorterDuff.Mode.SRC_IN);
                    Favourite favourite = new Favourite(customer_id, Utils.food.getFood_id(), Utils.food.getFood_name(), Utils.food.getFood_desc(), Utils.food.getFood_price(), Utils.food.getFood_img());
                    DatabaseFavourite.getInstance(getApplicationContext()).dao().insertFavorite(favourite);
                    Toast.makeText(getApplicationContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            imgLove.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.main), PorterDuff.Mode.SRC_IN);
            imgLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseFavourite.getInstance(getApplicationContext()).dao().deleteFavorite(favourite1);
                    imgLove.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_IN);
                    Toast.makeText(getApplicationContext(), "Đã xóa khỏi mục yêu thích", Toast.LENGTH_SHORT).show();
                }
            });
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void getData() {
        compositeDisposable.add(apiMain.getAllFood()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        foodAPI -> {
                            foodList = foodAPI.getData();
                            adapterSearch = new AdapterSearch(getApplicationContext(), foodList, iclickFood);
                            recyclerView.setAdapter(adapterSearch);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Loi Ket Noi Database", Toast.LENGTH_LONG).show();
                        }
                )
        );

    }


    private void initUi() {
        foodList = new ArrayList<>();
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recFoodSearch);
        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerFood);
        recyclerView.setHasFixedSize(true);
    }
}