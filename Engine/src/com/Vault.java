package com;

import Objects.interfaces.Safe;
import com.interfaces.DBInter;


public class Vault implements Safe {
    private DBInter db = DBHandler.Instance;

    public Vault() {

    }
}
