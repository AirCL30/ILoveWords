package com.icecream.cc.bean;

/**
 * Created by 陈湘龙 on 2020/5/20.
 */
public class Administrator {
    private String account;
    private String name;
    private String pwd;

    public Administrator() {
    }
    public Administrator(String account, String name, String pwd) {
        this.account = account;
        this.name = name;
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
