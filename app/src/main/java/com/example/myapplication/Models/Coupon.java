package com.example.myapplication.Models;

public class Coupon {
    String coupon_name;
    String coupon_code;
    int coupon_number;
    int coupon_condition;
    String coupon_start;
    String coupon_end;

    public Coupon(String coupon_code, int coupon_number, int coupon_condition) {
        this.coupon_code = coupon_code;
        this.coupon_number = coupon_number;
        this.coupon_condition = coupon_condition;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public int getCoupon_number() {
        return coupon_number;
    }

    public void setCoupon_number(int coupon_number) {
        this.coupon_number = coupon_number;
    }

    public int getCoupon_condition() {
        return coupon_condition;
    }

    public void setCoupon_condition(int coupon_condition) {
        this.coupon_condition = coupon_condition;
    }

    public String getCoupon_start() {
        return coupon_start;
    }

    public void setCoupon_start(String coupon_start) {
        this.coupon_start = coupon_start;
    }

    public String getCoupon_end() {
        return coupon_end;
    }

    public void setCoupon_end(String coupon_end) {
        this.coupon_end = coupon_end;
    }
}
