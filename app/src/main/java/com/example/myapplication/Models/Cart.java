package com.example.myapplication.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class Cart {
    @PrimaryKey(autoGenerate = true)
    int idFood;
    int customer_id;
    String nameFood;
    long priceFood;
    String imgFood;
    int qtyFood;

    public Cart(String nameFood, int customer_id,long priceFood, String imgFood, int qtyFood) {
        this.nameFood = nameFood;
        this.customer_id = customer_id;
        this.priceFood = priceFood;
        this.imgFood = imgFood;
        this.qtyFood = qtyFood;
    }

    public Cart() {

    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getIdFood() {
        return idFood;
    }

    public void setIdFood(int idFood) {
        this.idFood = idFood;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public long getPriceFood() {
        return priceFood;
    }

    public void setPriceFood(long priceFood) {
        this.priceFood = priceFood;
    }

    public String getImgFood() {
        return imgFood;
    }

    public void setImgFood(String imgFood) {
        this.imgFood = imgFood;
    }

    public int getQtyFood() {
        return qtyFood;
    }

    public void setQtyFood(int qtyFood) {
        this.qtyFood = qtyFood;
    }
}
