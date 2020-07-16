package com.example.petapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;

public abstract class DialogTranslucent extends Dialog {
    
    public DialogTranslucent(@NonNull Context context) {
        super(context);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        
    }
    
}
