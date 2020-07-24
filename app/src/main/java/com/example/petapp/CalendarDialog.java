package com.example.petapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Date;

public abstract class CalendarDialog extends DialogTranslucent {
    
    protected TextView eBtnAddTime;
    protected TextView eTxtMonth;
    protected EditText eEdtName;
    protected CustomCalendarView eCalendar;
    protected Date eDate;
    protected Event eEvent;
    private Activity eContext;
    private Bundle eBundle;
    private boolean eIsDone = false;
    private View eBtnAddEvent;
    private ImageView eBtnRightCorner;
    
    public CalendarDialog(Activity iActivity) {
        super(iActivity);
        this.eContext = iActivity;
        eBundle = new Bundle();
        
        
    }
    
    protected void bindViews() {
        eEdtName = findViewById(R.id.edt_add_event_name);
        eCalendar = findViewById(R.id.calendar_edit);
        eTxtMonth = findViewById(R.id.txt_month);
        eBtnAddEvent = findViewById(R.id.bt_add_event);
        eBtnAddTime = findViewById(R.id.bt_add_time);
        eBtnRightCorner = findViewById(R.id.bt_right_action);
        
    }
    
    protected void setUpAllListeners() {
        setUpButtonListeners();
        setUpCalendarListener();
        
    }
    
    private void setUpButtonListeners() {
        eBtnAddEvent.setOnClickListener(iView -> addEvent());
        eBtnAddTime.setOnClickListener(iView -> getDateFromTimePicker(eBtnAddTime));
        eBtnRightCorner.setOnClickListener(iView -> getRightCornerBtnAction());
        
    }
    
    @NonNull
    protected Activity getParentContext() {
        return eContext;
    }
    
    public boolean isDone() {
        return eIsDone;
    }
    
    public void setDone(boolean iDone) {
        eIsDone = iDone;
    }
    
    private void setDate(long iDateTime) {
        Date iNewDate = getDate();
        iNewDate.setTime(iDateTime);
        
    }
    
    public Bundle getBundle() {
        return eBundle;
    }
    
    protected void getDateFromTimePicker(TextView iTextView) {
        TimePickerDialog iDialog = new TimePickerDialog(getContext());
        iDialog.show();
        iDialog.setOnDismissListener(dialog -> {
            if (iDialog.isDone()) {
                String iTime = iDialog.getTime() + ":" + iDialog.getMinute();
                iTextView.setText(iTime);
                eBundle.putString("TIME", iTime);
            }
        });
        
    }
    
    protected void setMonthText(Date iDate) {
        eTxtMonth.setText(DateFormat.format("MMMM", iDate));
    }
    
    protected void sendBundle(Date iDate) {
        eEvent.setName(eEdtName.getText().toString().matches("") ? getParentContext().getString(R.string.TxtUnknownName) : eEdtName.getText().toString());
        eEvent.setMonth((String) DateFormat.format("MM", iDate));
        eEvent.setTime(eBtnAddTime.getText().toString());
        eEvent.setDay((String) DateFormat.format("dd", iDate));
        getBundle().putSerializable("CHANGED_EVENT", eEvent);
        setDone(true);
        dismiss();
        
    }
    
    private void setUpCalendarListener() {
        eCalendar.setListener(createCalendarListener());
    }
    
    private CustomCalendarView.CustomCalendarViewListener createCalendarListener() {
        return new CustomCalendarView.CustomCalendarViewListener() {
            
            @Override
            public void onDaySelected(Date iDate) {
                setDate(iDate.getTime());
            }
            
            @Override
            public void onScroll(Date iMonth) {
                setMonthText(iMonth);
            }
            
        };
        
    }
    
    protected abstract void addEvent();
    
    protected abstract void initializeDate();
    
    protected abstract void getRightCornerBtnAction();
    
    protected abstract Date getDate();
    
}

