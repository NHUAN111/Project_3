package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Models.Cart;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterCheckout extends BaseAdapter {
    Context context;
    List<Cart> cartList;

    public AdapterCheckout(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cartList.get(position).getIdFood();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cart cart = cartList.get(position);
        View viewLayout = View.inflate(context, R.layout.item_cart_dialog, null);
        TextView tvName = viewLayout.findViewById(R.id.tvItemNameFood);
        TextView tvQty = viewLayout.findViewById(R.id.tvItemQtyFood);
        TextView tvPrice = viewLayout.findViewById(R.id.tvItemPrice);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long priceTotal = cart.getPriceFood() * cart.getQtyFood();
        tvName.setText("- "+cart.getNameFood()+" ("+decimalFormat.format(cart.getPriceFood())+" đ)");
        tvQty.setText(" x "+cart.getQtyFood());
        tvPrice.setText(decimalFormat.format(priceTotal)+" đ");
        return viewLayout;
    }
}
