package com.icecream.cc.bean;

/**
 * Created by 陈湘龙 on 2020/5/13.
 */
public class User {
    private String utel;
    private String uname;
    private String upassword;

    public User(){

    }

    public User(String utel, String uname, String upassword) {
        this.utel = utel;
        this.uname = uname;
        this.upassword = upassword;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUtel() {
        return utel;
    }

    public void setUtel(String utel) {
        this.utel = utel;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }
}
