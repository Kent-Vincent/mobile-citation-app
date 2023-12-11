package com.example.capstone;

public class Accounts {
    private String Name;

    private String Email;
    private String AccountType;

    public Accounts(){

    }

    public Accounts(String name, String email, String accountType){
        this.Name = name;
        this.Email = email;
        this.AccountType = accountType;
    }

    public String getName(){
        return Name;
    }

    public void setName(String name){
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }
}
