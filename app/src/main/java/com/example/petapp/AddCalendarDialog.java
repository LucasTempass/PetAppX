package com.example.petapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddCalendarDialog extends CalendarDialog {
    
    private final String C_KEY_TIME = getParentContext().getString(R.string.KEY_TIME);
    
    public AddCalendarDialog(Activity a) {
        super(a);
        eEvent = new Event();
        initializeDate();
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calendar);
        bindViews();
        setUpAllListeners();
        setMonthText(eDate);
        
    }
    
    @Override
    protected void addEvent() {
        if (getBundle().get(C_KEY_TIME) != null)
            sendBundle(eDate);
        else
            Toast.makeText(getParentContext(), R.string.ToastSelectTime, Toast.LENGTH_LONG).show();
        
    }
    
    @Override
    protected void initializeDate() {
        eDate = Calendar.getInstance().getTime();
    }
    
    @Override
    protected void getRightCornerBtnAction() {
        dismiss();
    }
    
    @Override
    protected Date getDate() {
        return eDate;
    }
    
}

