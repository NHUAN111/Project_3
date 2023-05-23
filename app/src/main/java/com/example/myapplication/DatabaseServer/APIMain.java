package com.example.myapplication.DatabaseServer;

import com.example.myapplication.ModelApi.CategoryAPI;
import com.example.myapplication.ModelApi.CityAPI;
import com.example.myapplication.ModelApi.CouponAPI;
import com.example.myapplication.ModelApi.CustomerAPI;
import com.example.myapplication.ModelApi.FeeshipAPI;
import com.example.myapplication.ModelApi.FoodAPI;
import com.example.myapplication.ModelApi.OrderDetailAPI;
import com.example.myapplication.ModelApi.OrdersAPI;
import com.example.myapplication.ModelApi.PronviceAPI;
import com.example.myapplication.ModelApi.SliderAPI;
import com.example.myapplication.ModelApi.WardAPI;
import com.example.myapplication.Models.Cart;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIMain {
    @GET("api/web/customer")
    Observable<CustomerAPI> getAllCustomer();

    @GET("api/web/category")
    Observable<CategoryAPI> getAllCategory();

    @GET("api/web/food")
    Observable<FoodAPI> getAllFood();

    @GET("api/web/food_new")
    Observable<FoodAPI> getNewFood();

    @GET("api/web/food_bestseller")
    Observable<FoodAPI> getBestsellerFood();

    @GET("api/web/food_discount")
    Observable<FoodAPI> getDiscountFood();

    @GET("api/web/coupon")
    Observable<CouponAPI> getCoupon();

    @GET("api/web/slider")
    Observable<SliderAPI> getSlider();

    @GET("api/web/city")
    Observable<CityAPI> getCity();

    @GET("api/web/pronvice")
    Observable<PronviceAPI> getPronvice(
            @Query("name_city") String name_city
    );

    @GET("api/web/ward")
    Observable<WardAPI> getWard(
            @Query("name_province") String name_province
    );

    @GET("api/web/register")
    Observable<CustomerAPI> registerCustomerGet(
            @Query("customer_name") String customer_name,
            @Query("customer_email") String customer_email,
            @Query("customer_pass") String customer_pass,
            @Query("customer_phone") String customer_phone
    );

    @GET("api/web/login")
    Observable<CustomerAPI> loginCustomer(
            @Query("customer_name") String customer_name,
            @Query("customer_pass") String customer_pass
    );

    @GET("api/web/same_food")
    Observable<FoodAPI> sameFood(
            @Query("food_id") int food_id
    );

    @GET("api/web/category_id")
    Observable<FoodAPI> getCategoryById(
            @Query("category_id") int category_id
    );

    @GET("api/web/info_orders")
    Observable<OrdersAPI> info_orders(
            @Query("customer_id") int customer_id
    );

    @GET("api/web/cancel_order")
    Observable<OrdersAPI> cancel_order(
            @Query("order_code") String order_code
    );

    @GET("api/web/detail_order")
    Observable<OrderDetailAPI> detail_order(
            @Query("order_code") String order_code
    );

    @GET("api/web/shipping_fee")
    Observable<FeeshipAPI> shipping_fee(
            @Query("shipping_address") String shipping_address
    );

    @GET("api/web/check_coupon")
    Observable<CouponAPI> check_coupon(
            @Query("coupon_code") String coupon_code
    );

    @GET("api/web/order_detail")
    Observable<OrdersAPI> order_detail(
            @Query("customer_id") int customer_id,
            @Query("customer_name") String customer_name,
            @Query("customer_address") String customer_address,
            @Query("customer_phone") String customer_phone,
            @Query("customer_email") String customer_email,
            @Query("payment_method") int payment_method,
            @Query("coupon_price") String coupon_price,
            @Query("order_coupon") String order_coupon,
            @Query("order_feeship") String order_feeship,
//            @Query("cart") @Body List<Cart> cartList
            @Query("cart") String cart
    );
}
