package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.Customer;
import com.example.myapplication.Models.Feeship;

public class FeeshipAPI {
    String message;
    int status_code;
    Feeship data;

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

    public Feeship getData() {
        return data;
    }

    public void setData(Feeship data) {
        this.data = data;
    }
}
