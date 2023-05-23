package com.example.myapplication.OrderFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Adapter.AdapterOrders;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.Models.Orders;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderDeliveryTabFragment extends Fragment {
    Context context;
    View view;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIMain apiMain;
    List<Orders> ordersList;
    RecyclerView recyclerView;
    AdapterOrders adapterOrders;
    TextView tvNull;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_delivering_tab, container, false);
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        initUi(view);
        getData();
        reload();
        return view;
    }

    private void getData() {
        Customer customer = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER);
        int customer_id = customer.getCustomer_id();
        compositeDisposable.add(apiMain.info_orders(customer_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ordersAPI -> {
                            if (ordersAPI.getStatus_code() == 200) {
                                ordersList = ordersAPI.getData();
                                List<Orders> filteredOrdersList = new ArrayList<>();
                                for (Orders order : ordersList) {
                                    if (order.getOrder_status() == 1) {
                                        filteredOrdersList.add(order);
                                    } else if (filteredOrdersList.size() == 0) {
                                        tvNull.setText("Dữ liệu trống");
                                    }
                                }
                                adapterOrders = new AdapterOrders(context, filteredOrdersList);
                                recyclerView.setAdapter(adapterOrders);
                            } else if (ordersAPI.getStatus_code() == 202) {
                                tvNull.setText("Dữ liệu trống");
                            }
                        },
                        throwable -> {
                            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void reload() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void initUi(View view) {
        ordersList = new ArrayList<>();
        tvNull = view.findViewById(R.id.tvNull);
        recyclerView = view.findViewById(R.id.recOrderDelivering);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}