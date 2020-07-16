package com.example.petapp;

import java.io.Serializable;

public class PetMissing extends Pet implements Serializable {
    
    //TODO||| Get through  pet id in database  |||
    //private ArrayList<Bitmap> images;
    private String info;
    private double lat;
    private double lon;
    private Owner owner;
    private String id;
    
    public PetMissing(String iName, int iType, double iLat, double iLon) {
        super(iName, iType);
        this.lat = iLat;
        this.lon = iLon;
        
    }
    
    public PetMissing(Pet iPet, double iLat, double iLon) {
        super(iPet);
        this.lat = iLat;
        this.lon = iLon;
        
    }
    
    public double getLat() {
        return lat;
    }
    
    public double getLon() {
        return lon;
    }
    
    public void setID(String iID) {
        this.id = iID;
    }
    
}

