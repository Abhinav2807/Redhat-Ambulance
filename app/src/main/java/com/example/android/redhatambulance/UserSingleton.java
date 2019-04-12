package com.example.android.redhatambulance;

public class UserSingleton {

    private static UserSingleton sUserSingleton;

    public static UserSingleton get() {
        if (sUserSingleton == null) {
            sUserSingleton = new UserSingleton();
        }
        return sUserSingleton;
    }

    private UserSingleton(){

    }
    private String name;
    private String email;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

