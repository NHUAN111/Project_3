package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.RoomDatabase.DatabaseCart;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.Interface.IclickCart;
import com.example.myapplication.Models.Cart;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {
    Context context;
    List<Cart> cartList;
    IclickCart iclickCart;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();

    public AdapterCart(Context context, List cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    public AdapterCart(Context context, List<Cart> cartList, IclickCart iclickCart) {
        this.context = context;
        this.cartList = cartList;
        this.iclickCart = iclickCart;
    }

    private void setDataAdapter() {
        List<Cart> dataList = DatabaseCart.getInstance(context).Dao().getCartItemsForCustomer(customer_id);
        cartList = dataList;
        notifyDataSetChanged();
    }

//    public void setData(List<Cart> cartList){
//        this.cartList = cartList;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Cart cart = cartList.get(position);
        if(cart == null){
           return;
        }
        holder.tvNameFood.setText(cart.getNameFood());
        DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
        holder.tvPriceFood.setText(decimal_Format.format(cart.getPriceFood()) + " đ");
        holder.tvQtyFood.setText(cart.getQtyFood() + " ");
        Glide.with(context).load(cart.getImgFood()).into(holder.imageFood);
        long priceTotal = cart.getPriceFood() * cart.getQtyFood();
        holder.tvPriceFood2.setText(decimal_Format.format(priceTotal) + " đ");

//        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                repository = new Repository(context);
//                repository.delete(cart);
//                Toast.makeText(context, "Xoá thành công "+cart.getNameFood(), Toast.LENGTH_LONG).show();
//                setDataAdapter();
//            }
//        });


        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickCart.deleteCart(cart);
                setDataAdapter();
            }
        });

        holder.tvAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickCart.plusCart(cart);
                setDataAdapter();
            }
        });

        holder.tvMinusFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickCart.minusCart(cart);
                setDataAdapter();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(cartList.isEmpty()){
            return 0;
        }
        return cartList.size();
    }

    public void setData(List<Cart> data) {
        this.cartList = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameFood, tvPriceFood, tvQtyFood, tvPriceFood2, tvMinusFood, tvAddFood;
        ImageView imageFood, imageDelete;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFood = itemView.findViewById(R.id.itemNameFood);
            tvPriceFood = itemView.findViewById(R.id.itemPriceFood1);
            tvQtyFood = itemView.findViewById(R.id.itQtyFood);
            tvPriceFood2 = itemView.findViewById(R.id.itemPriceFood2);
            imageFood = itemView.findViewById(R.id.itemImgFood);
            imageDelete = itemView.findViewById(R.id.btnDeleteFood);
            tvMinusFood = itemView.findViewById(R.id.tvMinusFood);
            tvAddFood = itemView.findViewById(R.id.tvAddFood);
//            checkBox = itemView.findViewById(R.id.box_checked);
        }
    }
}
