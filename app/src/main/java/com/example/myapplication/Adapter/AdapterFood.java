package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.Interface.IclickFood;
import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.Models.Food;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterFood extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Food> foodList;
    IclickFood iclickFood;

    int VIEW_TYPE_ICON_TOP_RIGHT ;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VIEW_TYPE_SEARCH = 2;

    public AdapterFood(Context context, List<Food> foodList, int VIEW_TYPE_ICON_TOP_RIGHT, IclickFood iclickFood) {
        this.context = context;
        this.foodList = foodList;
        this.iclickFood = iclickFood;
        this.VIEW_TYPE_ICON_TOP_RIGHT = VIEW_TYPE_ICON_TOP_RIGHT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_food, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Food food = foodList.get(position);
            if (food.getFood_condition() == 1){
                myViewHolder.tvDiscountFood.setVisibility(View.VISIBLE);
                myViewHolder.tvDiscountFood.setText("- "+food.getFood_number()+" %");
            } else if (food.getFood_condition() == 2){
                myViewHolder.tvDiscountFood.setVisibility(View.VISIBLE);
                DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
                myViewHolder.tvDiscountFood.setText("- "+decimal_Format.format(Double.parseDouble(food.getFood_number()))+" đ");
            } else if (VIEW_TYPE_ICON_TOP_RIGHT == Constant.VIEW_TYPE_ICON_TOP_RIGHT_DEFAULT){
                myViewHolder.imgIconTopRight.setVisibility(View.GONE);
                myViewHolder.tvDiscountFood.setText("");
                myViewHolder.tvDiscountFood.setVisibility(View.GONE);
            } else if (VIEW_TYPE_ICON_TOP_RIGHT == Constant.VIEW_TYPE_ICON_TOP_RIGHT_HOT){
                myViewHolder.imgIconTopRight.setVisibility(View.VISIBLE);
                myViewHolder.imgIconTopRight.setImageResource(R.drawable.bestseller);
            } else if (VIEW_TYPE_ICON_TOP_RIGHT == Constant.VIEW_TYPE_ICON_TOP_RIGHT_NEW){
                myViewHolder.imgIconTopRight.setVisibility(View.VISIBLE);
                myViewHolder.imgIconTopRight.setImageResource(R.drawable.news);
            }

            myViewHolder.tvNameFood.setText(food.getFood_name());
            myViewHolder.tvTotalOrders.setText(food.getTotal_orders()+" đã bán");
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.tvPriceFood.setText(decimalFormat.format(Double.parseDouble(food.getFood_price())) + " đ");
            Glide.with(context).load(food.getFood_img()).into(myViewHolder.imgNameFood);
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean onClick) {
                    if (!onClick) {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("detail", food);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

            myViewHolder.btnAddFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.food = food;
                    iclickFood.addFood();
                }
            });
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemViewType(int position) {
        return foodList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
//            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNameFood, tvPriceFood, tvDiscountFood, tvTotalOrders;
        FrameLayout frameLayout;
        private ItemClickListener itemClickListener;
        ImageView imgNameFood, imgIconTopRight;
        Button btnAddFood;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvPriceFood = itemView.findViewById(R.id.tvPriceFood);
            imgNameFood = itemView.findViewById(R.id.imgName);
            frameLayout = itemView.findViewById(R.id.frameNewFood);
            tvDiscountFood = itemView.findViewById(R.id.tvDiscountFood);
            btnAddFood = itemView.findViewById(R.id.btnAddFood);
            imgIconTopRight = itemView.findViewById(R.id.imgIconTopRight);
            tvTotalOrders = itemView.findViewById(R.id.tvTotalOrders);
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
