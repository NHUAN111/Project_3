package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Interface.IclickFood;
import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.Models.Food;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterByCategory extends RecyclerView.Adapter<AdapterByCategory.MyViewHolder> {
    Context context;
    List<Food> foodList;
    IclickFood iclickFood;

    public AdapterByCategory(Context context, List<Food> foodList, IclickFood iclickFood) {
        this.context = context;
        this.foodList = foodList;
        this.iclickFood = iclickFood;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_by_category, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Food food = foodList.get(position);
            holder.tvName.setText(food.getFood_name());
            holder.tvOrderTotal.setText(food.getTotal_orders()+" đã bán");
            DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
            holder.tvPrice.setText(decimal_Format.format(Double.parseDouble(food.getFood_price())) + " đ");
            Glide.with(context).load(food.getFood_img()).into(holder.imgFood);
            holder.btnAddFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.food = food;
                    iclickFood.addFood();
                }
            });
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean onClick) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("detail", food);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvPrice, tvOrderTotal;
        private ItemClickListener itemClickListener;
        Button btnAddFood;
        ImageView imgFood;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.itemNameFood);
            tvPrice = itemView.findViewById(R.id.itemPriceFood);
            imgFood = itemView.findViewById(R.id.itemImgFood);
            btnAddFood = itemView.findViewById(R.id.btnAddFoods);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
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
