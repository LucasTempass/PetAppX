package com.example.petapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class AddPet extends PetActionActivity {
    
    private int C_ADD;
    private String C_KEY_ACTION;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpet);
        bindViews();
        initializeKeys();
        createTypeViewGroups();
        setUpSpinner();
        createHomeIntent();
        initializePet();
        //TODO||| EXTRACT keys |||
        getHomeIntent().putExtra("ACTION", C_ADD);
        setUpAllListeners();
        setInputFilter();
        
    }
    
    @Override
    protected void callTrySendingResult() {
        trySendingResult();
    }
    
    @Override
    protected void initializePet() {
        ePet = new Pet();
    }
    
    @Override
    protected void initializeKeys() {
        C_KEY_ACTION = getApplicationContext().getString(R.string.KEY_ACTION);
        C_ADD = 0;
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = intent.getData();
            //setImageTextView(getFileName(selectedImage));
            eTxtImg.setText(getFileName(selectedImage));
            //TODO||| EXTRACT keys |||
            getHomeIntent().putExtra("BITMAP", intent.getData());
            getHomeIntent().putExtra("FILENAME", getFileName(selectedImage));
        }
        
    }
    
}

