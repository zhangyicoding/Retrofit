package com.estyle.retrofit.bean;

public class LoginStateBean {

    private boolean stateCode;
    private String state;
    private String account;
    private String password;

    public boolean isStateCode() {
        return stateCode;
    }

    public void setStateCode(boolean stateCode) {
        this.stateCode = stateCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
