package com.example.petapp;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class TimePickerDialog extends DialogTranslucent {
    
    private boolean eIsDone = false;
    private String eTime;
    private String eMinute;
    private Calendar eCalendar;
    
    TimePickerDialog(@NonNull Context iContext) {
        super(iContext);
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_timepicker);
        eCalendar = Calendar.getInstance();
        TimePicker iPicker = findViewById(R.id.timePicker1);
        iPicker.setIs24HourView(true);
        iPicker.setOnTimeChangedListener((timePicker, i, i1) -> {
            eCalendar.set(Calendar.HOUR_OF_DAY, i);
            eCalendar.set(Calendar.MINUTE, i1);
        });
        View iBt_Add = findViewById(R.id.bt_add_time);
        iBt_Add.setOnClickListener(view -> {
            eIsDone = true;
            eTime = (String) DateFormat.format("HH", eCalendar);
            eMinute = (String) DateFormat.format("mm", eCalendar);
            dismiss();
        });
        View iBt_Dismiss = findViewById(R.id.bt_exit_time_dialog);
        iBt_Dismiss.setOnClickListener(view -> dismiss());
        
    }
    
    public boolean isDone() {
        return eIsDone;
    }
    
    public void setDone(boolean iDone) {
        eIsDone = iDone;
    }
    
    public String getTime() {
        return eTime;
    }
    
    public void setTime(String iTime) {
        eTime = iTime;
    }
    
    public String getMinute() {
        return eMinute;
    }
    
    public void setMinute(String iMinute) {
        eMinute = iMinute;
    }
    
    public Calendar getCalendar() {
        return eCalendar;
    }
    
    public void setCalendar(Calendar iCalendar) {
        eCalendar = iCalendar;
    }
    
}
