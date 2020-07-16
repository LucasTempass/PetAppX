package com.example.petapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Nullable;

public class Pet implements Parcelable {
    
    private String name;
    //||| True = Female |||
    private boolean gender;
    //||| Dog = 1 |||
    private int type;
    private String age;
    private String info;
    private String leashCode;
    private Bitmap image;
    private String fileName;
    
    public Pet() { }
    
    //||| Only for display purposes on map |||
    public Pet(String iName, int iType) {
        name = iName;
        type = iType;
        
    }
    
    public Pet(String iName, boolean iGender, String iAge, int iType, String iInfo, String iLeashCode, @Nullable Bitmap iImage, String iFileName) {
        name = iName;
        gender = iGender;
        age = iAge;
        type = iType;
        info = iInfo;
        leashCode = iLeashCode;
        image = iImage;
        fileName = iFileName;
        
    }
    
    public Pet(String iName, boolean iGender, String iAge, int iType) {
        this.name = iName;
        this.gender = iGender;
        this.age = iAge;
        this.type = iType;
        
    }
    
    protected Pet(Pet iPet) {
        name = iPet.getName();
        gender = iPet.isGender();
        age = iPet.getAge();
        type = iPet.getType();
        info = iPet.getInfo();
        leashCode = iPet.getCode();
        image = iPet.getImage();
        fileName = getFileName();
        
    }
    
    private Pet(Parcel in) {
        name = in.readString();
        gender = in.readByte() != 0;
        age = in.readString();
        type = in.readInt();
        info = in.readString();
        leashCode = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        fileName = in.readString();
        
    }
    
    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }
        
        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
        
    };
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public Bitmap getImage() {
        return image;
    }
    
    public void setImage(Bitmap image) {
        this.image = image;
    }
    
    public String getInfo() {
        return info;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }
    
    public String getCode() {
        return leashCode;
    }
    
    public void setCode(String code) {
        leashCode = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isGender() {
        return gender;
    }
    
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    
    public String getAge() {
        return age;
    }
    
    public void setAge(String xAge) {
        this.age = xAge;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    @Override
    public int describeContents() {
        return 0;
        
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(age);
        dest.writeInt(type);
        dest.writeString(info);
        dest.writeString(leashCode);
        dest.writeParcelable(image, flags);
        dest.writeString(fileName);
        
    }
    
}

