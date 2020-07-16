package com.example.petapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class EditPet extends PetActionActivity {
    
    private String C_KEY_PET;
    private String C_KEY_POSITION;
    private String C_KEY_BITMAP;
    private String C_KEY_ACTION;
    private int C_DELETE;
    private int C_EDIT;
    private boolean hasImageChanged;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpet);
        bindViews();
        initializeKeys();
        initializePet();
        createTypeViewGroups();
        setUpSpinner();
        setUpAllListeners();
        eBtnDelete.setVisibility(View.VISIBLE);
        eBtnDelete.setOnClickListener(v -> {
            getHomeIntent().putExtra("ACTION", C_DELETE);
            startActivity(getHomeIntent());
        });
        setUpFields();
        createHomeIntent();
        setInputFilter();
        getHomeIntent().putExtra(C_KEY_ACTION, C_EDIT);
        getHomeIntent().putExtra(C_KEY_POSITION, getIntent().getIntExtra(C_KEY_POSITION, 10));
        
    }
    
    private void setUpFields() {
        eEdtName.setText(ePet.getName());
        eSpinner.setSelection(ePet.isGender() ? 0 : 1);
        eEdtExtra.setText(ePet.getInfo());
        eTxtImg.setText(ePet.getFileName());
        eDate.setText("" + ePet.getAge());
        selectOriginalType(ePet.getType());
        
    }
    
    private void selectOriginalType(int iSelectedType) {
        eTypeViewGroups.get(iSelectedType).getConstraintLayout().performClick();
    }
    
    @Override
    protected void callTrySendingResult() {
        if (!hasImageChanged)
            getHomeIntent().putExtra(C_KEY_BITMAP, (Bitmap) getIntent().getParcelableExtra(C_KEY_BITMAP));
        trySendingResult();
        
    }
    
    @Override
    protected void initializePet() {
        ePet = getIntent().getParcelableExtra(C_KEY_PET);
    }
    
    @Override
    protected void initializeKeys() {
        C_KEY_PET = getString(R.string.KEY_PET);
        C_KEY_POSITION = getApplicationContext().getString(R.string.KEY_POSITION);
        C_KEY_BITMAP = getApplicationContext().getString(R.string.KEY_BITMAP);
        C_KEY_ACTION = getApplicationContext().getString(R.string.KEY_ACTION);
        C_DELETE = 2;
        C_EDIT = 1;
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            Uri iImage = intent.getData();
            hasImageChanged = true;
            eTxtImg.setText(getFileName(iImage));
            getHomeIntent().putExtra(C_KEY_BITMAP, intent.getData());
            ePet.setFileName(getFileName(iImage));
        }
        
    }
    
}

