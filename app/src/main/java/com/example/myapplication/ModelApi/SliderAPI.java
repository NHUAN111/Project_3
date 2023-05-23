package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.Slider;

import java.util.List;

public class SliderAPI {
    String message;
    int status_code;
    List<Slider> data;

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

    public List<Slider> getData() {
        return data;
    }

    public void setData(List<Slider> data) {
        this.data = data;
    }
}
