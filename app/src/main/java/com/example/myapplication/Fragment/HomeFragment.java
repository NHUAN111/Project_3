package com.example.myapplication.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.CartActivity;
import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Activity.SearchActivity;
import com.example.myapplication.Adapter.AdapterCoupon;
import com.example.myapplication.Adapter.AdapterFavourite;
import com.example.myapplication.Adapter.AdapterFood;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.Dao;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseFavourite;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Interface.IclickFavourite;
import com.example.myapplication.Interface.IclickFood;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Category;
import com.example.myapplication.Models.Coupon;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.Models.Favourite;
import com.example.myapplication.Models.Food;
import com.example.myapplication.Models.Slider;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    Context context;
    View view;
    Button btnAddFood;
    ViewFlipper viewFlipper;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIMain apiFood;
    ListView listViewHome;
    NotificationBadge badge;
    Toolbar toolbarHome;
    SearchView searchView;
    ImageView imgCart;
    TextView tvViewAllFood;

    List<Customer> customerList;
//    AdapterCustomer adapterMain;

    List<Food> foodList;
    RecyclerView recyclerViewFood, recyclerViewNewFood, recyclerViewDiscountFood, recyclerViewBestsellerFood;
    AdapterFood adapterFood;

    List<Coupon> couponList;
    RecyclerView recyclerViewCoupon;
    AdapterCoupon adapterCoupon;

    List<Slider> sliderList;

    IclickFood iClickFood;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    LinearLayout linearLayoutMain;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        apiFood = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        sharedPreferences = context.getApplicationContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        intUi();
        getData();

        FrameLayout frameLayout = view.findViewById(R.id.layout_toolbar);
        View view_toolbar = LayoutInflater.from(context).inflate(R.layout.layout_toolbar, frameLayout, false);
        badge = view_toolbar.findViewById(R.id.badges);
        imgCart = view_toolbar.findViewById(R.id.carts);
        searchView = view_toolbar.findViewById(R.id.searchView);

        List<Cart> data = DatabaseCart.getInstance(getContext()).Dao().getCartItemsForCustomer(customer_id);
        if (data.size() == 0) {
            badge.setText(0 + "");
        } else if (data.size() > 0) {
            badge.setText(data.size() + "");
        }

        frameLayout.addView(view_toolbar);
        enventClick();
        return view;
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_detail);
        Window window = dialog.getWindow();

        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wLayoutParams = window.getAttributes();
        window.setAttributes(wLayoutParams);
        wLayoutParams.gravity = Gravity.BOTTOM;

        if (Gravity.BOTTOM == Gravity.BOTTOM) {
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

        Cart data = DatabaseCart.getInstance(getContext()).Dao().searchCart(tvNameFood.getText().toString(), customer_id);
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
                    DatabaseCart.getInstance(getContext()).Dao().insert(cart);
                    loadCountCart();
                    Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    data.setPriceFood(data.getPriceFood());
                    int qty = Integer.parseInt(tvQtyFood.getText().toString());
                    data.setQtyFood(Integer.parseInt(data.getQtyFood() + "") + qty);
                    DatabaseCart.getInstance(getContext()).Dao().update(data);
                    Toast.makeText(getContext(), "Thêm Số Lượng Thành Công", Toast.LENGTH_LONG).show();
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
                    tvQtyFood.setText(String.valueOf(i));
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
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("detail", Utils.food);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Favourite favourite1 = DatabaseFavourite.getInstance(context).dao().getFavorite(Utils.food.getFood_name(), customer_id);
        if (favourite1 == null) {
            imgLove.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
            imgLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgLove.setColorFilter(ContextCompat.getColor(context, R.color.main), PorterDuff.Mode.SRC_IN);
                    Favourite favourite = new Favourite(customer_id, Utils.food.getFood_id(), Utils.food.getFood_name(), Utils.food.getFood_desc(), Utils.food.getFood_price(), Utils.food.getFood_img());
                    DatabaseFavourite.getInstance(context).dao().insertFavorite(favourite);
                    Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            imgLove.setColorFilter(ContextCompat.getColor(context, R.color.main), PorterDuff.Mode.SRC_IN);
            imgLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseFavourite.getInstance(context).dao().deleteFavorite(favourite1);
                    imgLove.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
                    Toast.makeText(context, "Đã xóa khỏi mục yêu thích", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // imageView.clearColorFilter();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void enventClick() {
        // Dialog Food
        iClickFood = new IclickFood() {
            @Override
            public void addFood() {
                openDialog();
            }
        };


        // Cart
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getContext(), CartActivity.class);
                startActivity(intent1);
            }
        });

        // Search
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // View All Food
        tvViewAllFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getData() {
        // Get Food
        compositeDisposable.add(apiFood.getAllFood()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        foodAPI -> {
                            foodList = foodAPI.getData();
                            adapterFood = new AdapterFood(getContext(), foodList, Constant.VIEW_TYPE_ICON_TOP_RIGHT_DEFAULT, iClickFood);
                            recyclerViewFood.setAdapter(adapterFood);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Loi Ket Noi Database Food", Toast.LENGTH_LONG).show();
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
                            adapterFood = new AdapterFood(getContext(), foodList, Constant.VIEW_TYPE_ICON_TOP_RIGHT_NEW, iClickFood);
                            recyclerViewNewFood.setAdapter(adapterFood);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Loi Ket Noi Database New Food", Toast.LENGTH_LONG).show();
                        }
                )
        );

        // Get Bestseller Food
        compositeDisposable.add(apiFood.getBestsellerFood()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        foodAPI -> {
                            foodList = foodAPI.getData();
                            adapterFood = new AdapterFood(getContext(), foodList, Constant.VIEW_TYPE_ICON_TOP_RIGHT_HOT, iClickFood);
                            recyclerViewBestsellerFood.setAdapter(adapterFood);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Loi Ket Noi Database New Food", Toast.LENGTH_LONG).show();
                        }
                )
        );

        // Get Food Discount (% - đ)
        compositeDisposable.add(apiFood.getDiscountFood()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        foodAPI -> {
                            foodList = foodAPI.getData();
                            adapterFood = new AdapterFood(getContext(), foodList, Constant.VIEW_TYPE_ICON_TOP_RIGHT_DEFAULT, iClickFood);
                            recyclerViewDiscountFood.setAdapter(adapterFood);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Loi Ket Noi Database Discount Food", Toast.LENGTH_LONG).show();
                        }
                )
        );

        // Get Coupon
        compositeDisposable.add(apiFood.getCoupon()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        couponAPI -> {
                            couponList = couponAPI.getData();
                            adapterCoupon = new AdapterCoupon(getContext(), couponList, Constant.VIEW_TYPE_ICON_COUPON_HOME);
                            recyclerViewCoupon.setAdapter(adapterCoupon);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Loi Ket Noi Database Category", Toast.LENGTH_LONG).show();
                        }
                )
        );

        // Get slider
        compositeDisposable.add(apiFood.getSlider()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sliderAPI -> {
                            sliderList = sliderAPI.getData();
                            for (int i = 0; i < sliderList.size(); i++) {
                                ImageView imageView = new ImageView(getContext());
                                Glide.with(getContext()).load(sliderList.get(i).getSlider_img()).into(imageView);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                viewFlipper.addView(imageView);
                            }
                        }, throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        viewFlipper.setAnimation(animation1);
        viewFlipper.setAnimation(animation2);


        Handler hd = new Handler();
        Runnable rn = new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                linearLayoutMain.setVisibility(View.VISIBLE);
            }
        };
        hd.postDelayed(rn, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCountCart();
    }

    private void intUi() {
        viewFlipper = view.findViewById(R.id.viewfipperHome);
        recyclerViewFood = view.findViewById(R.id.recycleviewFood);
        recyclerViewNewFood = view.findViewById(R.id.recycleviewNewFood);
        recyclerViewBestsellerFood = view.findViewById(R.id.recycleviewBestsellerFood);
        recyclerViewDiscountFood = view.findViewById(R.id.recycleviewDiscountFood);
        recyclerViewCoupon = view.findViewById(R.id.recItemCoupon);
        navigationView = view.findViewById(R.id.navigationview);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        btnAddFood = view.findViewById(R.id.btnAddFood);
        toolbarHome = view.findViewById(R.id.toolbarHome);
        progressBar = view.findViewById(R.id.progressBarHome);
        linearLayoutMain = view.findViewById(R.id.layoutMain);
        tvViewAllFood = view.findViewById(R.id.tvViewAllFood);

        customerList = new ArrayList<>();
        listViewHome = view.findViewById(R.id.listviewhome);


        // food
        foodList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFood.setLayoutManager(layoutManagerFood);
        recyclerViewFood.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManagerNewFood = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNewFood.setLayoutManager(layoutManagerNewFood);
        recyclerViewNewFood.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManagerDiscountFood = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDiscountFood.setLayoutManager(layoutManagerDiscountFood);
        recyclerViewDiscountFood.setHasFixedSize(true);

        RecyclerView.LayoutManager reLayoutManagerBestsellerFood = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBestsellerFood.setLayoutManager(reLayoutManagerBestsellerFood);
        recyclerViewBestsellerFood.setHasFixedSize(true);

        // coupon
        couponList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManagerCoupon = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManagerCoupon);
        recyclerViewCoupon.setHasFixedSize(true);

        // slider
        sliderList = new ArrayList<>();
    }

    // Check wifi
    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi != null && wifi.isConnected() || mobile != null && mobile.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void loadCountCart() {
        List<Cart> data = DatabaseCart.getInstance(getContext()).Dao().getCartItemsForCustomer(customer_id);
        if (data.size() == 0) {
            badge.setText(0 + "");
        } else if (data.size() > 0) {
            badge.setText(data.size() + "");
        }
    }

//    private void progressBarHome(){
//        Ultils.PROGRESSBAR_HOME_COUNT++;
//        if(Ultils.PROGRESSBAR_HOME_COUNT >= Ultils.TOTAL_CONTENT_HOME_COUNT){
//            progressBar.setVisibility(View.GONE);
//            linearLayoutMain.setVisibility(View.VISIBLE);
//        }
//
//    }

}