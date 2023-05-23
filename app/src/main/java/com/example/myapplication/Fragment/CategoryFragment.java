package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.CartActivity;
import com.example.myapplication.Activity.SearchActivity;
import com.example.myapplication.Adapter.AdapterCategory;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Category;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryFragment extends Fragment {
    Context context;
    View view;
    APIMain apiMain;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<Category> categoryList;
    RecyclerView recycleViewCategory;
    AdapterCategory adapterCategory;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    NotificationBadge badge;
    SearchView searchView;
    ImageView imgCart;

    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category_page, container, false);
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        intUi(view);
        getData();

        List<Cart> data = DatabaseCart.getInstance(getContext()).Dao().getCartItemsForCustomer(customer_id);
        FrameLayout frameLayout = view.findViewById(R.id.layout_toolbar);
        View view_toolbar = LayoutInflater.from(context).inflate(R.layout.layout_toolbar, frameLayout, false);
        badge = view_toolbar.findViewById(R.id.badges);
        imgCart = view_toolbar.findViewById(R.id.carts);
        searchView = view_toolbar.findViewById(R.id.searchView);
        badge.setText(data.size() + "");
        frameLayout.addView(view_toolbar);
        eventClick();
        return view;
    }

    private void eventClick() {
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

    }

    private void getData() {
        // Get Category
        compositeDisposable.add(apiMain.getAllCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryAPI -> {
                            categoryList = categoryAPI.getData();
                            adapterCategory = new AdapterCategory(getContext(), categoryList);
                            recycleViewCategory.setAdapter(adapterCategory);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Loi Ket Noi Database Category", Toast.LENGTH_LONG).show();
                        }
                )
        );
        Handler hd = new Handler();
        Runnable rb = new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        };
        hd.postDelayed(rb, 1000);

    }

    private void intUi(View view) {
        recycleViewCategory = view.findViewById(R.id.recCategory);
        progressBar = view.findViewById(R.id.progressBarHome);
        linearLayout = view.findViewById(R.id.layoutCategory);
        categoryList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManagerCategory = new GridLayoutManager(getContext(), 3);
        recycleViewCategory.setLayoutManager(layoutManagerCategory);
        recycleViewCategory.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCountCart();
    }

    private void loadCountCart() {
        List<Cart> data = DatabaseCart.getInstance(context).Dao().getCartItemsForCustomer(customer_id);
        if (data.size() == 0) {
            badge.setText(0 + "");
        } else {
            badge.setText(data.size() + "");
        }
    }
}