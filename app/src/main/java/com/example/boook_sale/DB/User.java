package com.example.boook_sale.DB;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String email;

    public User() {
        // Пустой конструктор необходим для Firestore
    }

    public User(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
