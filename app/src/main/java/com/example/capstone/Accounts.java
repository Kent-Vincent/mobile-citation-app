package com.example.capstone;

public class Accounts {
    private String Name;

    private String Email;

    public Accounts(){

    }

    public Accounts(String name, String email){
        this.Name = name;
        this.Email = email;
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
}
