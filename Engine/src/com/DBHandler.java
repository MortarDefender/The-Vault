package com;

import com.interfaces.DBInter;
import com.interfaces.UserInter;


public enum DBHandler implements DBInter {
    Instance;

    public UserInter getUser() {
        return new User();
    }

    public void addUser(UserInter newUser) {}

}
