package com.example.petapp;

import java.io.Serializable;

public class Evento implements Serializable {
    
    private String name;
    private Boolean isSet;
    private String month;
    private String day;
    private String time;
    
    public Evento() { }
    
    public Evento(String iName, Boolean isSet, String iMonth, String iDay, String iTime) {
        this.name = iName;
        this.isSet = isSet;
        this.month = iMonth;
        this.day = iDay;
        this.time = iTime;
        
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String iTime) {
        this.time = iTime;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String iName) {
        this.name = iName;
    }
    
    public Boolean isSet() {
        return isSet;
    }
    
    public void setSet(Boolean isSet) {
        this.isSet = isSet;
    }
    
    public String getMonth() {
        return month;
    }
    
    public void setMonth(String iMonth) {
        this.month = iMonth;
    }
    
    public String getDay() {
        return day;
    }
    
    public void setDay(String iDay) {
        this.day = iDay;
    }
    
}
