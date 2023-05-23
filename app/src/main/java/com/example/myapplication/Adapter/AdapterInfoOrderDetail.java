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
import com.example.myapplication.Models.OrderDetails;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterInfoOrderDetail extends RecyclerView.Adapter<AdapterInfoOrderDetail.MyViewHolder> {
    Context context;
    List<OrderDetails> orderDetailsList;

    public AdapterInfoOrderDetail(Context context, List<OrderDetails> orderDetailsList) {
        this.context = context;
        this.orderDetailsList = orderDetailsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderDetails orderDetails = orderDetailsList.get(position);
        holder.tvOrderNamFood.setText(orderDetails.getFood_name());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvOrderPrice.setText(decimalFormat.format(orderDetails.getFood_price())+" đ");
        holder.tvOrderQty.setText("Số lượng: "+orderDetails.getFood_sales_quantity());
        holder.tvOrderTotal.setText(decimalFormat.format(orderDetails.getTotal_price())+" đ");
        Glide.with(context).load(orderDetails.getFood_img()).into(holder.itemImgFood);
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvOrderNamFood, tvOrderPrice, tvOrderQty, tvOrderTotal;
        ImageView itemImgFood;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImgFood = itemView.findViewById(R.id.itemImgFood);
            tvOrderNamFood = itemView.findViewById(R.id.tvOrderNamFood);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvOrderQty = itemView.findViewById(R.id.tvOrderQty);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
        }
    }
}
