package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constant.Constant;
import com.example.myapplication.Interface.IclickCoupon;
import com.example.myapplication.Models.Coupon;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterCoupon extends RecyclerView.Adapter<AdapterCoupon.MyViewHolder> {
    Context context;
    List<Coupon> couponList;
    IclickCoupon iclickCoupon;
    int VIEW_TYPE_COUPON ;


    public AdapterCoupon(Context context, List<Coupon> couponList,  int VIEW_TYPE_COUPON) {
        this.context = context;
        this.couponList = couponList;
        this.VIEW_TYPE_COUPON = VIEW_TYPE_COUPON;
    }

    public AdapterCoupon(Context context, List<Coupon> couponList, int VIEW_TYPE_COUPON, IclickCoupon iclickCoupon) {
        this.context = context;
        this.couponList = couponList;
        this.iclickCoupon = iclickCoupon;
        this.VIEW_TYPE_COUPON = VIEW_TYPE_COUPON;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Coupon coupon = couponList.get(position);
        if (VIEW_TYPE_COUPON == Constant.VIEW_TYPE_ICON_COUPON_COUPON){
            holder.checkboxVoucher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        iclickCoupon.iclickCoupon(coupon);
                    }
                }
            });
        } else {
            holder.checkboxVoucher.setVisibility(View.GONE);
        }
        if(coupon.getCoupon_condition() == 1){
            holder.tvCouponNumber.setText(coupon.getCoupon_number()+" %");
        }else{
            DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
            holder.tvCouponNumber.setText(decimal_Format.format(coupon.getCoupon_number())+" đ");
        }
        holder.tvCouponName.setText(coupon.getCoupon_name());
        holder.tvCouponCode.setText(coupon.getCoupon_code());
        holder.tvCouponDay.setText(coupon.getCoupon_start()+" đến "+coupon.getCoupon_end());
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCouponNumber, tvCouponCode, tvCouponName, tvCouponDay;
        CheckBox checkboxVoucher;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCouponCode = itemView.findViewById(R.id.tvCouponCode);
            tvCouponName = itemView.findViewById(R.id.tvCouponName);
            tvCouponNumber = itemView.findViewById(R.id.tvCouponNumber);
            tvCouponDay = itemView.findViewById(R.id.tvCouponDay);
            checkboxVoucher = itemView.findViewById(R.id.checkboxVoucher);
        }
    }
}
