package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.Category;

import java.util.List;

public class CategoryAPI {
    String message;
    int status_code;
    List<Category> data;

    public CategoryAPI(String message, int status_code, List<Category> data) {
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

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }
}
