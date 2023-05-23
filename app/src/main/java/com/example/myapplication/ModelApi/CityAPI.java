package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.City;

import java.util.List;

public class CityAPI {
    String message;
    int status_code;
    List<City> data;

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

    public List<City> getData() {
        return data;
    }

    public void setCityList(List<City> cityList) {
        this.data = cityList;
    }
}
