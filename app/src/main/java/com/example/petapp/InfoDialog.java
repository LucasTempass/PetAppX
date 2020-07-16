package com.example.petapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class InfoDialog extends DialogTranslucent {
    
    private String eAdress;
    private String eName;
    
    public InfoDialog(@NonNull Activity iActivity, String iAdress, String iName) {
        super(iActivity);
        this.eAdress = iAdress;
        this.eName = iName;
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_white);
        ImageView iBt_Dismiss = findViewById(R.id.bt_dismiss_info);
        ImageView iBt_Email = findViewById(R.id.bt_message);
        ImageView iBt_Facebook = findViewById(R.id.bt_facebook);
        ImageView iBt_Twitter = findViewById(R.id.bt_twitter);
        ImageView iBt_Phone = findViewById(R.id.bt_phone);
        ImageView iImg_Photo = findViewById(R.id.img_photo);
        TextView iTxt_Adress = findViewById(R.id.txt_address_info);
        TextView iTxt_Name = findViewById(R.id.txt_name_info);
        if (!eName.matches(""))
            iTxt_Name.setText(eName);
        if (!eAdress.matches(""))
            iTxt_Adress.setText(eAdress);
        iBt_Email.setOnClickListener(i -> {
        
        });
        iBt_Facebook.setOnClickListener(i -> {
        
        });
        iBt_Twitter.setOnClickListener(i -> {
        
        });
        iBt_Phone.setOnClickListener(i -> {
        
        });
        iImg_Photo.setOnClickListener(iiew -> {
        
        });
        
        iBt_Dismiss.setOnClickListener(i -> dismiss());
        
    }
    
}
