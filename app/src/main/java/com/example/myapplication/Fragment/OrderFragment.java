package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.Activity.CartActivity;
import com.example.myapplication.Activity.SearchActivity;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.OrderFragment.OrderViewPagerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Widget.CustomViewPager;
import com.google.android.material.tabs.TabLayout;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

public class OrderFragment extends Fragment {
    Context context;
    View view;
    NotificationBadge badge;
    SearchView searchView;
    ImageView imgCart;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;

    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_page, container, false);
        intUi(view);

        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setTab();
        viewPager.setPagingEnabled(false);

        List<Cart> data = DatabaseCart.getInstance(context).Dao().getCartItemsForCustomer(customer_id);
        FrameLayout frameLayout = view.findViewById(R.id.layout_toolbar);
        View view_toolbar = LayoutInflater.from(context).inflate(R.layout.layout_toolbar, frameLayout, false);
        badge = view_toolbar.findViewById(R.id.badges);
        imgCart = view_toolbar.findViewById(R.id.carts);
        searchView = view_toolbar.findViewById(R.id.searchView);
        if (data.size() == 0) {
            badge.setText(0 + "");
        } else {
            badge.setText(data.size() + "");
        }
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

    private void intUi(View view) {
        tabLayout = view.findViewById(R.id.tab_layout_order);
        viewPager = view.findViewById(R.id.viewpager_order);
    }

    private void setTab() {
        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.RED);
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(gradientDrawable);
        }
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