package com.example.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PasswordRecovery extends AppCompatActivity {
    
    EditText eEmail;
    CardView eSend;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        eEmail = findViewById(R.id.editText6);
        eSend = findViewById(R.id.Bt_Send);
        eSend.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LogIn.class)));
        
    }
    
}
