package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.example.myapplication.Activity.AddDeliveryActivity;
import com.example.myapplication.Activity.DeliveryActivity;
import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseDelivery;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.Interface.ItemClickDelivery;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Delivery;
import com.example.myapplication.R;

import java.util.List;

public class AdapterDelivery extends RecyclerView.Adapter<AdapterDelivery.MyViewHolder> {
    Context context;
    List<Delivery> deliveryList;
    ItemClickDelivery itemClickDelivery;

    public AdapterDelivery(Context context, List<Delivery> deliveryList, ItemClickDelivery itemClickDelivery) {
        this.context = context;
        this.deliveryList = deliveryList;
        this.itemClickDelivery = itemClickDelivery;
    }

    public AdapterDelivery(Context context, List<Delivery> deliveryList) {
        this.context = context;
        this.deliveryList = deliveryList;
    }

    private void setDataAdapter() {
        List<Delivery> dataList = DatabaseDelivery.getInstance(context).dao().getListDelivery(MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id());
        deliveryList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterDelivery.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_delivery, parent, false);
        return new AdapterDelivery.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDelivery.MyViewHolder holder, int position) {
        Delivery delivery = deliveryList.get(position);
        holder.tvAddress.setText(delivery.getAddress());
        if(delivery.isStatus_delivery() == true){
            holder.imgCheck.setImageResource(R.drawable.baseline_radio_button_checked_24);
            holder.imgCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickDelivery.checkDelivery(delivery);
                    setDataAdapter();
                }
            });
        } else {
            holder.imgCheck.setImageResource(R.drawable.baseline_radio_button_unchecked_24);
            holder.imgCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickDelivery.checkDelivery(delivery);
                    setDataAdapter();
                }
            });
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickDelivery.deleteDelivery(delivery);
                setDataAdapter();
            }
        });

//        holder.imgFix.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, AddDeliveryActivity.class);
//                intent.putExtra("delivery", delivery);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgCheck, imgDelete;
        TextView tvAddress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            imgCheck = itemView.findViewById(R.id.imgCheckBoxAddress);
            imgDelete = itemView.findViewById(R.id.imgDeleteAddress);
//            imgFix = itemView.findViewById(R.id.imgFixAddress);

        }
    }
}
