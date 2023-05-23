package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.Customer;

public class CustomerAPI {
    String message;
    int status_code;
    Customer data;

    public CustomerAPI(String message, int status_code, Customer data) {
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

    public Customer getData() {
        return data;
    }

    public void setData(Customer data) {
        this.data = data;
    }
}
