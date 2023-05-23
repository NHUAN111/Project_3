package com.example.myapplication.Models;

public class OrderDetails {
    String order_code;
    String food_name;
    int food_price;
    int food_sales_quantity;
    String food_img;
    int total_price;
    Orders order;
    Shipping data_shipping;

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_price() {
        return food_price;
    }

    public void setFood_price(int food_price) {
        this.food_price = food_price;
    }

    public int getFood_sales_quantity() {
        return food_sales_quantity;
    }

    public void setFood_sales_quantity(int food_sales_quantity) {
        this.food_sales_quantity = food_sales_quantity;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getFood_img() {
        return food_img;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Shipping getData_shipping() {
        return data_shipping;
    }

    public void setData_shipping(Shipping data_shipping) {
        this.data_shipping = data_shipping;
    }
}
