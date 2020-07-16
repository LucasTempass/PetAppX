package com.example.petapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class LogIn extends Activity {
    
    CardView eBtnSignUp;
    TextView eTxtPass;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eBtnSignUp = findViewById(R.id.Bt_LogIn);
        eTxtPass = findViewById(R.id.Bt_Go_To_Password);
        eTxtPass.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), PasswordRecovery.class)));
        eBtnSignUp.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NewHome.class)));
        
    }
    
}
