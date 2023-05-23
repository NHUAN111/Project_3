package com.example.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.AdapterFavourite;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseFavourite;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.Interface.IclickFavourite;
import com.example.myapplication.Models.Favourite;
import com.example.myapplication.R;

import java.util.List;

public class FavouriteFragment extends Fragment {
    Context context;
    View view;
    AdapterFavourite adapterFavourite;
    RecyclerView recyclerView;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourite_page, container, false);
        initUi();
        getData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavouriteData();
    }

    private void getData() {
        List<Favourite> favouriteList = DatabaseFavourite.getInstance(context).dao().getListFavorite(customer_id);
        if (favouriteList.size() == 0){
            textView.setText("Dữ liệu trống");
        } else {
            textView.setText("");
        }
        adapterFavourite = new AdapterFavourite(getContext(), favouriteList, new IclickFavourite() {
            @Override
            public void onClickItem(Favourite favourite) {
                Favourite favourite1 = DatabaseFavourite.getInstance(context).dao().getFavorite(favourite.getFood_name(), customer_id);
                if (favourite1 != null) {
                    DatabaseFavourite.getInstance(context).dao().deleteFavorite(favourite1);
                    Toast.makeText(context, "Đã xóa khỏi mục yêu thích", Toast.LENGTH_SHORT).show();
                    loadFavouriteData();
                }
            }
        });
        recyclerView.setAdapter(adapterFavourite);
    }

    private void loadFavouriteData(){
        List<Favourite> favouriteList = DatabaseFavourite.getInstance(context).dao().getListFavorite(customer_id);
        if (favouriteList.size() == 0){
            textView.setText("Dữ liệu trống");
        } else {
            textView.setText("");
        }
        adapterFavourite = new AdapterFavourite(getContext(), favouriteList, new IclickFavourite() {
            @Override
            public void onClickItem(Favourite favourite) {
                Favourite favourite1 = DatabaseFavourite.getInstance(context).dao().getFavorite(favourite.getFood_name(), customer_id);
                if (favourite1 != null) {
                    DatabaseFavourite.getInstance(context).dao().deleteFavorite(favourite1);
                    Toast.makeText(context, "Đã xóa khỏi mục yêu thích", Toast.LENGTH_SHORT).show();
                    loadFavouriteData();
                }
            }
        });
        recyclerView.setAdapter(adapterFavourite);
    }

    private void initUi() {
        recyclerView = view.findViewById(R.id.recAllFavourite);
        textView = view.findViewById(R.id.tvFavouriteNull);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}