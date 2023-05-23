package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Food;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterDetailFood extends RecyclerView.Adapter<AdapterDetailFood.MyViewHolder> {
    Context context;
    List<Food> foodList;

    public AdapterDetailFood(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_by_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Food food = foodList.get(position);
        if (food.getFood_condition() == 1) {
            holder.tvDiscount.setText("- " + food.getFood_number() + " %");
        } else if (food.getFood_condition() == 2) {
            DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
            holder.tvDiscount.setText("- " + decimal_Format.format(Double.parseDouble(food.getFood_number())) + " đ");
        } else {
            holder.tvDiscount.setText("");
            holder.tvDiscount.setVisibility(View.GONE);

        }
        holder.tvNameFood.setText(food.getFood_name());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPriceFood.setText(decimalFormat.format(Double.parseDouble(food.getFood_price())) + "đ");
        Glide.with(context).load(food.getFood_img()).into(holder.imgNameFood);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameFood, tvPriceFood, tvDiscount;
        ImageView imgNameFood;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFood = itemView.findViewById(R.id.imgName);
            tvPriceFood = itemView.findViewById(R.id.itemPriceFood);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            imgNameFood = itemView.findViewById(R.id.itemNameFood);
        }
    }
}
