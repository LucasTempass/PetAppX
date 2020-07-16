package com.example.petapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditCalendarDialog extends CalendarDialog {
    
    private Date eNewDate;
    private boolean eIsDeleted = false;
    
    public EditCalendarDialog(Activity iActivity, Bundle iBundle) {
        super(iActivity);
        eEvent = (Evento) iBundle.getSerializable(iActivity.getString(R.string.KEY_EVENT));
        initializeDate();
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calendar);
        bindViews();
        setUpViews();
        setUpAllListeners();
        
    }
    
    private void delete() {
        eIsDeleted = true;
        dismiss();
        
    }
    
    public boolean wasDeleted() {
        return eIsDeleted;
    }
    
    private void setUpViews() {
        ConstraintLayout iLayout = findViewById(R.id.layout_dialog_edit_event);
        iLayout.setTouchscreenBlocksFocus(true);
        setUpFields();
        
    }
    
    private void setUpFields() {
        eEdtName.setText(eEvent.getName());
        eCalendar.setDate(eDate);
        eCalendar.invalidate();
        eBtnAddTime.setText(eEvent.getTime());
        setMonthText(eDate);
        
    }
    
    @Override
    public void addEvent() {
        sendBundle(eNewDate != null ? eNewDate : eDate);
    }
    
    @Override
    protected void initializeDate() {
        java.text.DateFormat iDate = new SimpleDateFormat("HH:mmddMMMMYYYY");
        try {
            eDate = iDate.parse(eEvent.getTime() + eEvent.getDay() + eEvent.getMonth());
        }
        catch (ParseException ignored) {
            eDate = Calendar.getInstance().getTime();
        }
        eNewDate = new Date();
        
    }
    
    @Override
    protected void getRightCornerBtnAction() {
        delete();
    }
    
    @Override
    protected Date getDate() {
        return eNewDate;
    }
    
}

