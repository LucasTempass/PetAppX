package com.example.petapp;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class LeashCodeAdd extends LeashCodeAction {
    
    ArrayList<String> eList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        //TODO Check before
        bindViewGroup();
        startUp();
        eArrayList = new ArrayList<>();
        eArrayList2 = new ArrayList<>();
        
    }
    
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (isLeashCode()) {
            if (isValidCode() && !isTaken()) {
                Intent iIntent = new Intent(getApplicationContext(), NewHome.class);
                iIntent.putExtra("NUMBER_2", text);
                startActivity(iIntent);
                return;
            }
        }
        
        Toast.makeText(getApplicationContext(), R.string.MsgInvalidQRCode, Toast.LENGTH_LONG).show();
        
    }
    
    private boolean isTaken() {
        return false;
    }
    
}

