package com.example.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Enter extends AppCompatActivity {
    
    TextView eTxtClick;
    CardView eBtnSignUp;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        eBtnSignUp = findViewById(R.id.Bt_SignUp);
        eTxtClick = findViewById(R.id.Bt_Go_To_Password);
        eTxtClick.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LogIn.class)));
        eBtnSignUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewHome.class)));
        
    }
    
}
