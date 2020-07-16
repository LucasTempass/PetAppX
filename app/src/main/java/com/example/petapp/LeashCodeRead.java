package com.example.petapp;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

public class LeashCodeRead extends LeashCodeAction {
    
    // Activity check
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        bindViewGroup();
        startUp();
        
    }
    
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (isLeashCode()) {
            if (isValidCode()) {
                Intent intent = new Intent(getApplicationContext(), NewHome.class);
                intent.putExtra("NUMBER", text);
                startActivity(intent);
            }
            else
                Toast.makeText(getApplicationContext(), R.string.MsgCodeNotRegistered, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getApplicationContext(), R.string.MsgInvalidQR, Toast.LENGTH_LONG).show();
        
    }
    
}

