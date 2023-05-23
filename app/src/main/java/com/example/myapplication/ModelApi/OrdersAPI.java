package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.Orders;

import java.util.List;

public class OrdersAPI {
    String message;
    int status_code;
    List<Orders> data;

    public OrdersAPI(String message, int status_code, List<Orders> data) {
        this.message = message;
        this.status_code = status_code;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public List<Orders> getData() {
        return data;
    }

    public void setData(List<Orders> data) {
        this.data = data;
    }
}
