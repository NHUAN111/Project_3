package com.example.myapplication.Models;

import java.io.Serializable;
import java.util.List;

public class Orders implements Serializable {
    String order_code;
    String order_coupon;
    String coupon_price;
    String order_feeship;
    int customer_id;
    int shipping_id;
    int payment_id;
    int order_status;
    String created_at;
    String updated_at;
    Shipping shipping_detail;
    Payment payment_detail;
    List<OrderDetails> order_detail;

    public Orders(String order_code, String order_coupon, String coupon_price, String order_feeship, int customer_id, int shipping_id, int payment_id, int order_status, String created_at, String updated_at, Shipping shipping_detail, Payment payment_detail, List<OrderDetails> order_detail) {
        this.order_code = order_code;
        this.order_coupon = order_coupon;
        this.coupon_price = coupon_price;
        this.order_feeship = order_feeship;
        this.customer_id = customer_id;
        this.shipping_id = shipping_id;
        this.payment_id = payment_id;
        this.order_status = order_status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.shipping_detail = shipping_detail;
        this.payment_detail = payment_detail;
        this.order_detail = order_detail;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_coupon() {
        return order_coupon;
    }

    public void setOrder_coupon(String order_coupon) {
        this.order_coupon = order_coupon;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getOrder_feeship() {
        return order_feeship;
    }

    public void setOrder_feeship(String order_feeship) {
        this.order_feeship = order_feeship;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(int shipping_id) {
        this.shipping_id = shipping_id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Shipping getShipping_detail() {
        return shipping_detail;
    }

    public void setShipping_detail(Shipping shipping_detail) {
        this.shipping_detail = shipping_detail;
    }

    public Payment getPayment_detail() {
        return payment_detail;
    }

    public void setPayment_detail(Payment payment_detail) {
        this.payment_detail = payment_detail;
    }

    public List<OrderDetails> getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(List<OrderDetails> order_detail) {
        this.order_detail = order_detail;
    }
}
