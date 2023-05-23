package com.example.myapplication.Fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPager2Adapter extends FragmentStateAdapter {
    Context context;
    public MyViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new CategoryFragment();
            case 2:
                return new OrderFragment();
            case 3:
                return new FavouriteFragment();
            case 4:
                return new PersonalFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
