package com.example.myapplication.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

public class CustomTablayout extends TabLayout {

    public CustomTablayout(@NonNull Context context) {
        super(context);
    }

    public CustomTablayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTablayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        int chilCount = viewGroup.getChildCount();
        if (chilCount != 0) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            int tabMinWidth = displayMetrics.widthPixels / chilCount;
            for (int i = 0; i < chilCount; i++) {
                viewGroup.getChildAt(i).setMinimumWidth(tabMinWidth);

            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
