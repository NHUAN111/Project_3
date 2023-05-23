package com.example.myapplication.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class Favourite {
    @PrimaryKey(autoGenerate = true)
    int favorite_id;
    @ColumnInfo(name = "customer_id")
    int customer_id;
    @ColumnInfo(name = "food_id")
    int food_id;
//    @ColumnInfo(name = "category_id")
//    int category_id;
//    @ColumnInfo(name = "category_name")
//    String category_name;
    @ColumnInfo(name = "food_name")
    String food_name;
    @ColumnInfo(name = "food_desc")
    String food_desc;
    @ColumnInfo(name = "food_price")
    String food_price;
    @ColumnInfo(name = "food_image")
    String food_image;

    public Favourite(int customer_id, int food_id, String food_name, String food_desc, String food_price, String food_image) {
        this.customer_id = customer_id;
        this.food_id = food_id;
//        this.category_id = category_id;
//        this.category_name = category_name;
        this.food_name = food_name;
        this.food_desc = food_desc;
        this.food_price = food_price;
        this.food_image = food_image;
    }

    public int getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

//    public int getCategory_id() {
//        return category_id;
//    }
//
//    public void setCategory_id(int category_id) {
//        this.category_id = category_id;
//    }

//    public String getCategory_name() {
//        return category_name;
//    }
//
//    public void setCategory_name(String category_name) {
//        this.category_name = category_name;
//    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_desc() {
        return food_desc;
    }

    public void setFood_desc(String food_desc) {
        this.food_desc = food_desc;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getFood_image() {
        return food_image;
    }

    public void setFood_image(String food_image) {
        this.food_image = food_image;
    }
}
