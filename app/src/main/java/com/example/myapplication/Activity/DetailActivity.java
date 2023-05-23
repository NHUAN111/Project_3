package com.example.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.AdapterFood;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseFavourite;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Interface.IclickFood;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.Models.Favourite;
import com.example.myapplication.Models.Food;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {
    TextView tvNameFood, tvPriceFood, tvDiscount;
    ImageView imageViewFood, btnEnd;
    RecyclerView recyclerViewSameFood, recNewFood;
    Spinner spinner;
    Food food;
    AdapterFood adapterFood;
    List<Food> foodList;
    Button btnAddFood;
    APIMain apiFood;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ImageView imgCart;
    IclickFood iclickFood;
    NotificationBadge badge;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        apiFood = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        intView();
        actionBar();
        initData();
        initControl();
        loadCountCart();
        iclickFood = new IclickFood() {
            @Override
            public void addFood() {
                openDialog(Gravity.BOTTOM);
            }
        };
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
                    loadCountCart();
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


    private void initControl() {
        // Add food
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
                loadCountCart();
            }
        });

        // Intent cart
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addCart() {
        Cart data = DatabaseCart.getInstance(getApplicationContext()).Dao().searchCart(tvNameFood.getText().toString(), customer_id);
        if (data == null) {
            int qty = Integer.parseInt(spinner.getSelectedItem().toString());
            long price = Long.parseLong(food.getFood_price());
            Cart cart = new Cart();
            cart.setPriceFood(price);
            cart.setCustomer_id(customer_id);
            cart.setQtyFood(qty);
            cart.setNameFood(food.getFood_name());
            cart.setIdFood(food.getFood_id());
            cart.setImgFood(food.getFood_img());
            DatabaseCart.getInstance(getApplicationContext()).Dao().insert(cart);
            loadCountCart();
            Toast.makeText(this, "Thêm Sản Phẩm Thành Công", Toast.LENGTH_LONG).show();
        } else {
            int qty = Integer.parseInt(spinner.getSelectedItem().toString()) + data.getQtyFood();
            data.setPriceFood(data.getPriceFood());
            data.setQtyFood(qty);
            DatabaseCart.getInstance(getApplicationContext()).Dao().update(data);
            Toast.makeText(this, "Thêm Số Lượng Món Thành Công", Toast.LENGTH_LONG).show();
        }
    }

    private void intView() {
        tvDiscount = findViewById(R.id.tvDiscount);
        tvNameFood = findViewById(R.id.tvNameFoodDetail);
        tvPriceFood = findViewById(R.id.tvPriceFoodDetail);
        btnAddFood = findViewById(R.id.btnAddFoodDetail);
        spinner = findViewById(R.id.spiner);
        imageViewFood = findViewById(R.id.imgDetail);
        recyclerViewSameFood = findViewById(R.id.recSameFood);
        btnEnd = findViewById(R.id.btnEnd);
        imgCart = findViewById(R.id.imgCart);
        badge = findViewById(R.id.badge);
        recNewFood = findViewById(R.id.recNewFood);

        // new food
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recNewFood.setLayoutManager(layoutManager);
        recNewFood.setHasFixedSize(true);

        // food
        foodList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSameFood.setLayoutManager(layoutManagerFood);
        recyclerViewSameFood.setHasFixedSize(true);
    }

    private void initData() {
        food = (Food) getIntent().getSerializableExtra("detail");
        if (food.getFood_condition() == 1) {
            tvDiscount.setText("- " + food.getFood_number() + " %");
        } else if (food.getFood_condition() == 2) {
            DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
            tvDiscount.setText("- " + decimal_Format.format(Double.parseDouble(food.getFood_number())) + " đ");
        } else {
            tvDiscount.setText("");
            tvDiscount.setVisibility(View.GONE);
        }
        tvNameFood.setText(food.getFood_name());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvPriceFood.setText("Giá: " + decimalFormat.format(Double.parseDouble(food.getFood_price())) + " đ");
        Glide.with(getApplicationContext()).load(food.getFood_img()).into(imageViewFood);
        Integer[] integers = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> integerArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, integers);
        spinner.setAdapter(integerArrayAdapter);

        // same food
        compositeDisposable.add(apiFood.sameFood(food.getFood_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        foodAPI -> {
                            foodList = foodAPI.getData();
                            adapterFood = new AdapterFood(getApplicationContext(), foodList, Constant.VIEW_TYPE_ICON_TOP_RIGHT_DEFAULT, new IclickFood() {
                                @Override
                                public void addFood() {
                                    openDialog(Gravity.BOTTOM);
                                }
                            });
                            recyclerViewSameFood.setAdapter(adapterFood);
                        },
                        throwable -> {
                            Toast.makeText(this, "Loi Ket Noi Database Food", Toast.LENGTH_LONG).show();
                        }
                )
        );

        // Get New Food
        compositeDisposable.add(apiFood.getNewFood()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        foodAPI -> {
                            foodList = foodAPI.getData();
                            adapterFood = new AdapterFood(getApplication(), foodList, Constant.VIEW_TYPE_ICON_TOP_RIGHT_NEW, iclickFood);
                            recNewFood.setAdapter(adapterFood);
                        },
                        throwable -> {
                            Toast.makeText(getApplication(), "Loi Ket Noi Database New Food", Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void actionBar() {
        loadCountCart();
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCountCart();
    }

    private void loadCountCart() {
        List<Cart> data = DatabaseCart.getInstance(getApplicationContext()).Dao().getCartItemsForCustomer(customer_id);
        if (data.size() == 0) {
            badge.setText(0 + "");
        } else if (data.size() > 0) {
            badge.setText(data.size() + "");
        }
    }

}