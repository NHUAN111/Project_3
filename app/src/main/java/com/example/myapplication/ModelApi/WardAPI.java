package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.Ward;

import java.util.List;

public class WardAPI {
    String message;
    int status_code;
    List<Ward> data;

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

    public List<Ward> getWardList() {
        return data;
    }

    public void setWardList(List<Ward> wardList) {
        this.data = wardList;
    }
}
