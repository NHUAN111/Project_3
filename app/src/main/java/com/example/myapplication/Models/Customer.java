package com.example.myapplication.Models;

public class Customer{
    int customer_id;
    String customer_name;
    String customer_email;
    String customer_pass;
    String customer_phone;

    public Customer(int customer_id, String customer_name, String customer_email, String customer_pass, String customer_phone) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_pass = customer_pass;
        this.customer_phone = customer_phone;
    }

    public Customer() {
    }

    public Customer(String customer_name, String customer_email, String customer_pass, String customer_phone) {
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_pass = customer_pass;
        this.customer_phone = customer_phone;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_pass() {
        return customer_pass;
    }

    public void setCustomer_pass(String customer_pass) {
        this.customer_pass = customer_pass;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }
}
