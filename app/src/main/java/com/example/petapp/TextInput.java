package com.example.petapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

public class TextInput implements TextWatcher {
    
    private String iCurrent = "";
    private String iFormat = "DDMMAAAA";
    private Calendar iCalendar = Calendar.getInstance();
    private EditText iEdt;
    
    public TextInput(EditText iEdt) {
        this.iEdt = iEdt;
        this.iEdt.addTextChangedListener(this);
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals(iCurrent)) { return; }
        String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
        String cleanC = iCurrent.replaceAll("[^\\d.]|\\.", "");
        
        int cl = clean.length();
        int sel = cl;
        for (int i = 2; i <= cl && i < 6; i += 2) {
            sel++;
        }
        //Fix for pressing delete next to a forward slash
        if (clean.equals(cleanC))
            sel--;
        
        if (clean.length() < 8) {
            clean = clean + iFormat.substring(clean.length());
        }
        else {
            //This part makes sure that when we finish entering numbers
            //the date is correct, fixing it otherwise
            int day = Integer.parseInt(clean.substring(0, 2));
            int mon = Integer.parseInt(clean.substring(2, 4));
            int year = Integer.parseInt(clean.substring(4, 8));
            
            mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
            iCalendar.set(Calendar.MONTH, mon - 1);
            year = (year < 1980) ? 1980 : (year > 2019) ? 2019 : year;
            iCalendar.set(Calendar.YEAR, year);
            // ^ first set year for the line below to work correctly
            //with leap years - otherwise, date e.g. 29/02/2012
            //would be automatiiCalendarly corrected to 28/02/2012
            
            day = (day > iCalendar.getActualMaximum(Calendar.DATE)) ? iCalendar.getActualMaximum(Calendar.DATE) : day;
            clean = String.format("%02d%02d%02d", day, mon, year);
        }
        
        clean = String.format("%s/%s/%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8));
        
        sel = sel < 0 ? 0 : sel;
        iCurrent = clean;
        iEdt.setText(iCurrent);
        iEdt.setSelection(sel < iCurrent.length() ? sel : iCurrent.length());
        
    }
    
    @Override
    public void afterTextChanged(Editable s) {
    
    }
    
}