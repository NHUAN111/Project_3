package com.example.myapplication.ModelApi;

import com.example.myapplication.Models.Pronvice;

import java.util.List;

public class PronviceAPI {
    String message;
    int status_code;
    List<Pronvice> data;

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

    public List<Pronvice> getPronviceList() {
        return data;
    }

    public void setPronviceList(List<Pronvice> pronviceList) {
        this.data = pronviceList;
    }
}
