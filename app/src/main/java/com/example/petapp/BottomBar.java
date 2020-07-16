package com.example.petapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

public class BottomBar extends CoordinatorLayout {
    
    BottomSheetBehavior eBehavior = null;
    CoordinatorLayout eLayout;
    TextView eTxt_Name;
    TextView eTxt_Address;
    ProgressBar eProgressBar;
    
    public BottomBar(Context context) {
        super(context);
        start(context);
        
    }
    
    public BottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        start(context);
        
    }
    
    public BottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        start(context);
        
    }
    
    private void start(Context context) {
        eLayout = (CoordinatorLayout) LayoutInflater.from(context).inflate(R.layout.view_popup, this);
        eBehavior = BottomSheetBehavior.from(eLayout.findViewById(R.id.root_bottom_sheet));
        eBehavior.setHideable(true);
        eBehavior.setState(STATE_HIDDEN);
        eTxt_Name = findViewById(R.id.TxtDescription);
        eTxt_Address = findViewById(R.id.TxtTitle);
        eProgressBar = findViewById(R.id.progress_bar_place);
        
    }
    
    public boolean isVisible() {
        return eBehavior.getState() != STATE_HIDDEN;
        
    }
    
    void load() {
        if (!isVisible())
            toggle();
        eTxt_Name.setText("");
        eTxt_Address.setText("");
        eProgressBar.setVisibility(View.VISIBLE);
        
    }
    
    void toggle() {
        eBehavior.setPeekHeight(eLayout.findViewById(R.id.LayoutBottom).getHeight());
        eBehavior.setState(isVisible() ? STATE_HIDDEN : STATE_COLLAPSED);
        
    }
    
    void setAdress(String iTitle, String iDescription) {
        eProgressBar.setVisibility(View.INVISIBLE);
        //eTxt_Name.setText(iTitle.isEmpty() ? R.string.MsgErrorLocation : iTitle);
        //eTxt_Address.setText(iTitle.isEmpty() ? R.string.MsgErrorGps : iDescription);
        if (iTitle.isEmpty()) {
            eTxt_Name.setText(R.string.MsgErrorLocation);
            eTxt_Address.setText(R.string.MsgErrorGps);
        }
        else {
            eTxt_Name.setText(iTitle);
            eTxt_Address.setText(iDescription);
        }
        
    }
    
}