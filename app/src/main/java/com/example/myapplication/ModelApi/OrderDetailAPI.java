package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.OrderDetails;
import com.example.myapplication.Models.Orders;

import java.util.List;

public class OrderDetailAPI {
    String message;
    int status_code;
    List<OrderDetails> data;

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

    public List<OrderDetails> getData() {
        return data;
    }

    public void setData(List<OrderDetails> data) {
        this.data = data;
    }
}
