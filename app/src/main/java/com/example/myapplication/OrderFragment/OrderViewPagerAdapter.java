package com.example.myapplication.OrderFragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class OrderViewPagerAdapter extends FragmentStatePagerAdapter {
    Context context;

    public OrderViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OrderProcessingTabFragment();
            case 1:
                return new OrderDeliveryTabFragment();
            case 2:
                return new OrderCompletedTabFragment();
            case 3:
                return new OrderCancelledTabFragment();
            default:
                return new OrderProcessingTabFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Đang xử lý";
            case 1:
                return "Đang giao";
            case 2:
                return "Hoàn thành";
            case 3:
                return "Đã huỷ";
            default:
                return "Đang xử lý";
        }
    }
}
