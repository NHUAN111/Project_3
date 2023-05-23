package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseFavourite;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.Interface.IclickFavourite;
import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Favourite;
import com.example.myapplication.Models.Food;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterFavourite extends RecyclerView.Adapter<AdapterFavourite.MyViewHolder> {
    Context context;
    List<Favourite> favouriteList;
    IclickFavourite iclickFavourite;

    public AdapterFavourite(Context context, List<Favourite> favouriteList, IclickFavourite iclickFavourite) {
        this.context = context;
        this.favouriteList = favouriteList;
        this.iclickFavourite = iclickFavourite;
    }

    private void setDataAdapter() {
        List<Favourite> dataList = DatabaseFavourite.getInstance(context).dao().getListFavorite(MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id());
        favouriteList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterFavourite.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_favourite, parent, false);
        return new AdapterFavourite.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavourite.MyViewHolder holder, int position) {
        Favourite favourite = favouriteList.get(position);
        holder.tvNameFood.setText(favourite.getFood_name());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPriceFood.setText(decimalFormat.format(Double.parseDouble(favourite.getFood_price()+""))+" đ");
        Glide.with(context).load(favourite.getFood_image()).into(holder.imgNameFood);
        holder.imgIconFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickFavourite.onClickItem(favourite);
                setDataAdapter();
            }
        });
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int pos, boolean onClick) {
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("detail", String.valueOf(food));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvNameFood, tvPriceFood, tvDiscountFood;
        FrameLayout frameLayout;
        ImageView imgNameFood, imgIconFavourite;
        Button btnAddFood;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvPriceFood = itemView.findViewById(R.id.tvPriceFood);
            imgNameFood = itemView.findViewById(R.id.imgName);
            frameLayout = itemView.findViewById(R.id.frameNewFood);
            tvDiscountFood = itemView.findViewById(R.id.tvDiscountFood);
            btnAddFood = itemView.findViewById(R.id.btnAddFood);
            imgIconFavourite = itemView.findViewById(R.id.imgIconFavourite);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
