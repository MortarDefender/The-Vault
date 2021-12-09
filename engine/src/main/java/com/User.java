package com;

import com.interfaces.UserInter;

public class User implements UserInter {
    private String username, password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return password; }

    @Override
    public void setUsername(String newUsername) { this.username = newUsername; }

    @Override
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
