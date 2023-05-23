package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.InfoOrderDetailActivity;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Interface.IclickOrder;
import com.example.myapplication.Models.OrderDetails;
import com.example.myapplication.Models.Orders;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.MyViewHolder> {
    Context context;
    List<Orders> ordersList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIMain apiMain;
    IclickOrder iclickCancelOrder;
    List<OrderDetails> orderDetailsList;
    int customer_id = MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_id();

    public AdapterOrders(Context context, List<Orders> ordersList, IclickOrder iclickCancelOrder) {
        this.context = context;
        this.ordersList = ordersList;
        this.iclickCancelOrder = iclickCancelOrder;
    }

    public AdapterOrders(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        if (holder instanceof AdapterOrders.MyViewHolder) {
            AdapterOrders.MyViewHolder myViewHolder = (AdapterOrders.MyViewHolder) holder;
            Orders orders = ordersList.get(position);
            myViewHolder.tvOrderCode.setText("Mã đơn hàng: "+orders.getOrder_code());
            myViewHolder.tvOrderDate.setText(orders.getCreated_at());
            if (orders.getOrder_status() == 0) {
                myViewHolder.tvPayment.setText("Hình thức thanh toán: tiền mặt");
                myViewHolder.tvPaymentMethod.setText("Trạng thái: chưa thanh toán");
                myViewHolder.imgDetailOrder.setImageResource(R.drawable.baseline_clear_24);
                myViewHolder.tvOrderDate.setText(orders.getCreated_at());
                compositeDisposable.add(apiMain.detail_order(orders.getOrder_code())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ordersAPI -> {
                                    if (ordersAPI.getStatus_code() == 200) {
                                        int qtyFood = 0;
                                        int subtotal = 0;
                                        orderDetailsList = ordersAPI.getData();
                                        for (int i = 0; i < orderDetailsList.size(); i++) {
                                            int total_price = orderDetailsList.get(i).getTotal_price();
                                            qtyFood = orderDetailsList.size();
                                            subtotal += total_price;
                                        }
                                        Glide.with(context).load(orderDetailsList.get(0).getFood_img()).into(myViewHolder.imgFoodOrder);
                                        myViewHolder.tvQtyFood.setText("Số lượng: " + orderDetailsList.get(0).getFood_sales_quantity());
                                        myViewHolder.tvNameFood.setText(ordersAPI.getData().get(0).getFood_name());
                                        int fee_ship = Integer.parseInt(orderDetailsList.get(0).getOrder().getOrder_feeship());
                                        DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
                                        myViewHolder.tvPriceFood.setText(decimal_Format.format(Double.parseDouble(String.valueOf(orderDetailsList.get(0).getFood_price()))) + " đ");
                                        myViewHolder.tvQtyFoodAndFeeShip.setText(qtyFood + " sản phẩm " + " + phí ship");
                                        myViewHolder.tvTotalOrder.setText(decimal_Format.format(Double.parseDouble(String.valueOf(subtotal + fee_ship))) + " đ");
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        ));
                myViewHolder.imgDetailOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iclickCancelOrder.CancelOrder(orders);
                    }
                });

                myViewHolder.imgDetailOrderProccessing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, InfoOrderDetailActivity.class);
                        intent.putExtra("order_code", orders.getOrder_code());
                        context.startActivity(intent);
                    }
                });
            } else if (orders.getOrder_status() == 4) {
                myViewHolder.tvPayment.setText("Hình thức thanh toán: tiền mặt");
                myViewHolder.tvPaymentMethod.setText("Trạng thái: đã thanh toán");
                myViewHolder.imgDetailOrder.setVisibility(View.GONE);
                myViewHolder.tvOrderDate.setText(orders.getCreated_at());
                compositeDisposable.add(apiMain.detail_order(orders.getOrder_code())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ordersAPI -> {
                                    if (ordersAPI.getStatus_code() == 200) {
                                        int qtyFood = 0;
                                        int subtotal = 0;
                                        orderDetailsList = ordersAPI.getData();
                                        for (int i = 0; i < orderDetailsList.size(); i++) {
                                            int total_price = orderDetailsList.get(i).getTotal_price();
                                            qtyFood = orderDetailsList.size();
                                            subtotal += total_price;
                                        }
                                        Glide.with(context).load(orderDetailsList.get(0).getFood_img()).into(myViewHolder.imgFoodOrder);
                                        myViewHolder.tvQtyFood.setText("Số lượng: " + orderDetailsList.get(0).getFood_sales_quantity());
                                        myViewHolder.tvNameFood.setText(ordersAPI.getData().get(0).getFood_name());
                                        int fee_ship = Integer.parseInt(orderDetailsList.get(0).getOrder().getOrder_feeship());
                                        DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
                                        myViewHolder.tvPriceFood.setText(decimal_Format.format(Double.parseDouble(String.valueOf(orderDetailsList.get(0).getFood_price()))) + " đ");
                                        myViewHolder.tvQtyFoodAndFeeShip.setText(qtyFood + " sản phẩm " + " + phí ship");
                                        myViewHolder.tvTotalOrder.setText(decimal_Format.format(Double.parseDouble(String.valueOf(subtotal + fee_ship))) + " đ");
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        ));
                myViewHolder.imgDetailOrderProccessing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, InfoOrderDetailActivity.class);
                        intent.putExtra("order_code", orders.getOrder_code());
                        context.startActivity(intent);
                    }
                });
            } else {
                myViewHolder.tvPayment.setText("Hình thức thanh toán: tiền mặt");
                myViewHolder.tvPaymentMethod.setText("Trạng thái: chưa thanh toán");
                myViewHolder.imgDetailOrder.setVisibility(View.GONE);
                myViewHolder.tvOrderDate.setText(orders.getCreated_at());
                compositeDisposable.add(apiMain.detail_order(orders.getOrder_code())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ordersAPI -> {
                                    if (ordersAPI.getStatus_code() == 200) {
                                        int qtyFood = 0;
                                        int subtotal = 0;
                                        orderDetailsList = ordersAPI.getData();
                                        for (int i = 0; i < orderDetailsList.size(); i++) {
                                            int total_price = orderDetailsList.get(i).getTotal_price();
                                            qtyFood = orderDetailsList.size();
                                            subtotal += total_price;
                                        }
                                        Glide.with(context).load(orderDetailsList.get(0).getFood_img()).into(myViewHolder.imgFoodOrder);
                                        myViewHolder.tvQtyFood.setText("Số lượng: " + orderDetailsList.get(0).getFood_sales_quantity());
                                        myViewHolder.tvNameFood.setText(ordersAPI.getData().get(0).getFood_name());
                                        int fee_ship = Integer.parseInt(orderDetailsList.get(0).getOrder().getOrder_feeship());
                                        DecimalFormat decimal_Format = new DecimalFormat("###,###,###");
                                        myViewHolder.tvPriceFood.setText(decimal_Format.format(Double.parseDouble(String.valueOf(orderDetailsList.get(0).getFood_price()))) + " đ");
                                        myViewHolder.tvQtyFoodAndFeeShip.setText(qtyFood + " sản phẩm " + " + phí ship");
                                        myViewHolder.tvTotalOrder.setText(decimal_Format.format(Double.parseDouble(String.valueOf(subtotal + fee_ship))) + " đ");
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        ));
                myViewHolder.imgDetailOrderProccessing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, InfoOrderDetailActivity.class);
                        intent.putExtra("order_code", orders.getOrder_code());
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCode, tvOrderDate, tvPayment, tvPaymentMethod, tvNameFood, tvPriceFood, tvQtyFood, tvQtyFoodAndFeeShip, tvTotalOrder;
        ImageView imgFoodOrder, imgDetailOrder, imgDetailOrderProccessing;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvPayment = itemView.findViewById(R.id.tvPayment);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            imgDetailOrder = itemView.findViewById(R.id.imgDetailOrder);
            imgFoodOrder = itemView.findViewById(R.id.imgFoodOrder);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvPriceFood = itemView.findViewById(R.id.tvPriceFood);
            tvQtyFood = itemView.findViewById(R.id.tvQtyFood);
            tvQtyFoodAndFeeShip = itemView.findViewById(R.id.tvQtyFoodAndFeeShip);
            tvTotalOrder = itemView.findViewById(R.id.tvTotalOrder);
            imgDetailOrderProccessing = itemView.findViewById(R.id.imgDetailOrderProccessing);
        }
    }


}
