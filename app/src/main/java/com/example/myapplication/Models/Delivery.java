package com.example.myapplication.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "delivery")

public class Delivery  implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    int customer_id;
    String address;
    boolean status_delivery;

    int fee_feeship;

    public Delivery(int customer_id, String address, boolean status_delivery) {
        this.customer_id = customer_id;
        this.address = address;
        this.status_delivery = status_delivery;
    }

    public Delivery(){

    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public boolean isStatus_delivery() {
        return status_delivery;
    }

    public void setStatus_delivery(boolean status_delivery) {
        this.status_delivery = status_delivery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFee_feeship() {
        return fee_feeship;
    }

    public void setFee_feeship(int fee_feeship) {
        this.fee_feeship = fee_feeship;
    }
}
