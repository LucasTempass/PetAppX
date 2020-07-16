package com.example.petapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    
    public SpinnerAdapter(Context iContext, List<String> iObjects, int iLayoutResId) {
        super(iContext, iLayoutResId, iObjects);
    }
    
    @Override
    public int getCount() {
        return super.getCount() > 0 ? super.getCount() - 1 : super.getCount();
    }
    
}