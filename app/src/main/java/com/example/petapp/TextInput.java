package com.example.petapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class TextInput implements TextWatcher {
    
    private String currentContent = "";
    private String C_FORMAT = "DDMMAAAA";
    private Calendar currentDate = Calendar.getInstance();
    private EditText eEditText;
    
    public TextInput(EditText iEdt) {
        this.eEditText = iEdt;
        this.eEditText.addTextChangedListener(this);
        
    }
    
    @Override
    public void onTextChanged(CharSequence iInput, int iStart, int iBefore, int iCount) {
        if (iInput.toString().equals(currentContent)) { return; }
        String cleanInput = iInput.toString().replaceAll("[^\\d.]|\\.", "");
        String cleanCurrent = currentContent.replaceAll("[^\\d.]|\\.", "");
        int inputLength = cleanInput.length();
        int sel = inputLength;
        for (int i = 2; i <= inputLength && i < 6; i += 2)
            sel++;
        if (cleanInput.equals(cleanCurrent))
            sel--;
        if (cleanInput.length() < 8)
            cleanInput += C_FORMAT.substring(cleanInput.length());
        else {
            int day = Integer.parseInt(cleanInput.substring(0, 2));
            int mon = Integer.parseInt(cleanInput.substring(2, 4));
            int year = Integer.parseInt(cleanInput.substring(4, 8));
            mon = mon < 1 ? 1 : Math.min(mon, 12);
            currentDate.set(Calendar.MONTH, mon - 1);
            year = Math.min(year, currentDate.get(Calendar.YEAR));
            currentDate.set(Calendar.YEAR, year);
            day = Math.min(day, currentDate.getActualMaximum(Calendar.DATE));
            cleanInput = String.format(Locale.ENGLISH, "%02d%02d%02d", day, mon, year);
        }
        cleanInput = String.format("%s/%s/%s", cleanInput.substring(0, 2), cleanInput.substring(2, 4), cleanInput.substring(4, 8));
        sel = Math.max(sel, 0);
        currentContent = cleanInput;
        eEditText.setText(currentContent);
        eEditText.setSelection(Math.min(sel, currentContent.length()));
        
    }
    
    @Override
    public void beforeTextChanged(CharSequence iCharSequence, int iStart, int iCount, int iAfter) { }
    
    @Override
    public void afterTextChanged(Editable iEditable) { }
    
}