package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.Fragment.MyViewPager2Adapter;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intUi();

        MyViewPager2Adapter adapter = new MyViewPager2Adapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setEnabled(false);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.action_category).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.action_order).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.action_love).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.action_personal).setChecked(true);
                        break;
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;

                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.action_category:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.action_order:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.action_love:
                        viewPager2.setCurrentItem(3);
                        break;
                    case R.id.action_personal:
                        viewPager2.setCurrentItem(4);
                        break;
                }
                return false;
            }
        });


    }

    private void intUi() {
        viewPager2 = findViewById(R.id.viewPage2);
        bottomNavigationView = findViewById(R.id.bottomNav);
        viewPager2.setUserInputEnabled(false);
    }
}

