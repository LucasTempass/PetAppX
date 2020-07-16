package com.example.petapp;

import java.io.Serializable;

public class Owner implements Serializable {
    
    private int id;
    private String name;
    private String twitter, facebook, phone, password, email;
    
    public Owner(int iId, String iEmail, String iFacebook, String iName, String iPassword, String iPhone, String iTwitter) {
        id = iId;
        email = iEmail;
        twitter = iTwitter;
        phone = iPhone;
        facebook = iFacebook;
        name = iName;
        password = iPassword;
        
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int iId) {
        this.id = iId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String iName) {
        name = iName;
    }
    
    public String getTwitter() {
        return twitter;
    }
    
    public void setTwitter(String iTwitter) {
        twitter = iTwitter;
    }
    
    public String getFacebook() {
        return facebook;
    }
    
    public void setFacebook(String iFacebook) {
        facebook = iFacebook;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String iPhone) {
        phone = iPhone;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String iPassword) {
        password = iPassword;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String iEmail) {
        email = iEmail;
    }
    
}
