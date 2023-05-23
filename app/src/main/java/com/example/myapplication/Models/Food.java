package com.example.myapplication.Models;

import java.io.Serializable;

public class Food implements Serializable {
    int food_id;
    String food_name;
    String food_desc;
    String food_content;
    int category_id;
    String food_price;
    String food_img;
    int food_condition;
    String food_number;
    int food_status;

    int total_orders;



    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getFood_img() {
        return food_img;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }

    public int getFood_status() {
        return food_status;
    }

    public void setFood_status(int food_status) {
        this.food_status = food_status;
    }

    public String getFood_desc() {
        return food_desc;
    }

    public void setFood_desc(String food_desc) {
        this.food_desc = food_desc;
    }

    public String getFood_content() {
        return food_content;
    }

    public void setFood_content(String food_content) {
        this.food_content = food_content;
    }

    public int getFood_condition() {
        return food_condition;
    }

    public void setFood_condition(int food_condition) {
        this.food_condition = food_condition;
    }

    public String getFood_number() {
        return food_number;
    }

    public void setFood_number(String food_number) {
        this.food_number = food_number;
    }

    public int getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(int total_orders) {
        this.total_orders = total_orders;
    }
}
