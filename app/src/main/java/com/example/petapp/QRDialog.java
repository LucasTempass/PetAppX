package com.example.petapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

public class QRDialog extends DialogTranslucent {
    
    private Activity eActivity;
    
    public QRDialog(Activity iActivity) {
        super(iActivity);
        this.eActivity = iActivity;
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_scanner);
        CardView iBt = findViewById(R.id.Bt_Scan);
        iBt.setOnClickListener(v -> eActivity.startActivity(new Intent(eActivity, LeashCodeAdd.class)));
        
    }
    
}
