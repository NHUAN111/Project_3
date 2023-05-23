package com.example.myapplication.Models;

import java.io.Serializable;

public class Category implements Serializable {
    int category_id;
    String category_name;
    String category_img;
    int category_status;

    public Category(int category_id, String category_name, String category_img, int category_status) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_img = category_img;
        this.category_status = category_status;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_img() {
        return category_img;
    }

    public void setCategory_img(String category_img) {
        this.category_img = category_img;
    }

    public int getCategory_status() {
        return category_status;
    }

    public void setCategory_status(int category_status) {
        this.category_status = category_status;
    }
}
