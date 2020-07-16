package com.example.petapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class PetActionActivity extends Activity {
    
    private final int C_DOG = 0;
    private final int C_CAT = 1;
    private final int C_OTHER = 2;
    private final int C_NONE = -1;
    protected int eSelectedType = C_NONE;
    @SuppressWarnings("FieldCanBeLocal")
    private String C_KEY_PET = "PET";
    
    protected List<TypeViewGroup> eTypeViewGroups;
    protected ImageView eBtnReturn;
    protected TextView eTxtImg;
    protected EditText eEdtName;
    protected EditText eEdtExtra;
    protected Spinner eSpinner;
    protected EditText eDate;
    protected CardView eBtnAdd;
    protected CardView eBtnDelete;
    protected ConstraintLayout eBtnPick;
    protected Pet ePet;
    private Intent eIntent;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    protected String getName() throws PetException {
        String iName = this.eEdtName.getText().toString();
        if (isDifferent(iName, ""))
            return iName;
        else
            throw new PetException("NAME");
        
    }
    
    protected boolean getGender() throws PetException {
        String iGender = eSpinner.getSelectedItem().toString();
        if (isDifferent(iGender, getApplicationContext().getString(R.string.TxtGender)))
            return iGender.matches(getApplicationContext().getString(R.string.TxtFemale));
        else
            throw new PetException(getApplicationContext().getString(R.string.MsgSelectGender));
        
    }
    
    protected String getDate() throws PetException {
        String iDate = eDate.getText().toString();
        if (isValidDate(iDate))
            return iDate;
        else
            throw new PetException(getApplicationContext().getString(R.string.MsgInvalidDate));
        
    }
    
    protected String getLeashCode() {
        return getApplicationContext().getString(R.string.TxtDefaultLeashCode);
    }
    
    protected int getType() throws PetException {
        if (isValidType(eSelectedType))
            return eSelectedType;
        else
            throw new PetException(getApplicationContext().getString(R.string.MsgSelectSpecies));
        
    }
    
    protected String getExtra() {
        String iExtra = eEdtExtra.getText().toString();
        return isDifferent(iExtra, "") ? iExtra : getApplicationContext().getString(R.string.TxtDefaultExtra);
        
    }
    
    protected Intent getHomeIntent() {
        return eIntent;
    }
    
    protected void createHomeIntent() {
        eIntent = new Intent(getApplicationContext(), NewHome.class);
    }
    
    //TODO ||| REMAKE |||
    protected String getFileName(Uri iUri) {
        String iResult = null;
        if (iUri.getScheme().equals("content"))
            try (Cursor iCursor = getContentResolver().query(iUri, null, null, null, null)) {
                if (iCursor != null && iCursor.moveToFirst())
                    iResult = iCursor.getString(iCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        if (iResult == null) {
            iResult = iUri.getPath();
            int iCut = iResult.lastIndexOf('/');
            if (iCut != -1)
                iResult = iResult.substring(iCut + 1);
        }
        return iResult;
        
    }
    
    //TODO ||| OPTIMIZE |||
    private boolean isValidDate(String iDate2Check) {
        Date iDate;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try { iDate = formatter.parse(iDate2Check); }
        catch (ParseException ignored) {
            return false;
        }
        Calendar iBirthday = new GregorianCalendar();
        iBirthday.setTime(iDate);
        Calendar iToday = new GregorianCalendar();
        iToday.setTime(new Date());
        int iYearsDif = iToday.get(Calendar.YEAR) - iBirthday.get(Calendar.YEAR);
        int iMonthsDif = iToday.get(Calendar.MONTH) - iBirthday.get(Calendar.MONTH);
        long iAgeInMonths = iYearsDif * 12 + iMonthsDif;
        long iAgeInDays = iToday.get(Calendar.DAY_OF_YEAR) - iBirthday.get(Calendar.DAY_OF_YEAR);
        if ((long) iYearsDif == 0) {
            if (iAgeInMonths < 0)
                return false;
            if (iAgeInMonths == 0)
                return iAgeInDays >= 0;
            else
                return true;
        }
        else
            return true;
        
    }
    
    private boolean isDifferent(String iString2Check, String iPattern) {
        return !iString2Check.matches(iPattern);
    }
    
    private boolean isValidType(int iType) {
        return iType != C_NONE;
    }
    
    protected void bindViews() {
        eBtnReturn = findViewById(R.id.Bt_Add);
        eSpinner = findViewById(R.id.Spin_Sex);
        eDate = findViewById(R.id.Edt_Date);
        eBtnDelete = findViewById(R.id.Bt_Delete);
        eEdtExtra = findViewById(R.id.editText3);
        eEdtName = findViewById(R.id.Edt_Name);
        eTxtImg = findViewById(R.id.Txt_Img);
        eBtnAdd = findViewById(R.id.Bt_Add_Pet);
        eBtnPick = findViewById(R.id.Bt_Pick);
        
    }
    
    protected void createTypeViewGroups() {
        eTypeViewGroups = new ArrayList<TypeViewGroup>() {{
            add(new TypeViewGroup(getApplicationContext(),
                    findViewById(R.id.Txt_Dog), findViewById(R.id.Img_Dog),
                    findViewById(R.id.Card_Dog), findViewById(R.id.Bt_Dog)
            ));
            add(new TypeViewGroup(getApplicationContext(),
                    findViewById(R.id.Txt_Cat), findViewById(R.id.Img_Cat),
                    findViewById(R.id.Card_Cat), findViewById(R.id.Bt_Cat)
            ));
            add(new TypeViewGroup(getApplicationContext(),
                    findViewById(R.id.Txt_O), findViewById(R.id.Img_O),
                    findViewById(R.id.Card_O), findViewById(R.id.Bt_O)
            ));
        }};
        
    }
    
    protected void setUpSpinner() {
        ArrayList<String> iSex = new ArrayList<>();
        iSex.add(getApplicationContext().getString(R.string.TxtFemale));
        iSex.add(getApplicationContext().getString(R.string.TxtMale));
        iSex.add(getApplicationContext().getString(R.string.TxtGender));
        SpinnerAdapter iAdapter = new SpinnerAdapter(getApplicationContext(), iSex, R.layout.item_spinner2);
        eSpinner.setAdapter(iAdapter);
        
    }
    
    protected void setInputFilter() {
        new TextInput(eDate);
    }
    
    protected void setUpAllListeners() {
        setUpTypeListeners();
        setUpButtonListeners();
        
    }
    
    private void setUpTypeListeners() {
        eTypeViewGroups.get(C_DOG).getConstraintLayout().setOnClickListener(v -> {
            eTypeViewGroups.get(C_DOG).toggle();
            setSelectedAndDismissOthers(C_DOG);
        });
        eTypeViewGroups.get(C_CAT).getConstraintLayout().setOnClickListener(v -> {
            eTypeViewGroups.get(C_CAT).toggle();
            setSelectedAndDismissOthers(C_CAT);
        });
        eTypeViewGroups.get(C_OTHER).getConstraintLayout().setOnClickListener(v -> {
            eTypeViewGroups.get(C_OTHER).toggle();
            setSelectedAndDismissOthers(C_OTHER);
        });
        
    }
    
    private void setUpButtonListeners() {
        eBtnReturn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewHome.class)));
        eBtnPick.setOnClickListener(v -> startActivityForImage());
        eBtnAdd.setOnClickListener(v -> callTrySendingResult());
        
    }
    
    private void setSelectedAndDismissOthers(int iCode) {
        if (eSelectedType == iCode) {
            eSelectedType = C_NONE;
            return;
        }
        else if (eSelectedType != C_NONE) {
            eTypeViewGroups.get(eSelectedType).toggle();
        }
        eSelectedType = iCode;
        
    }
    
    private void startActivityForImage() {
        Intent iIntentImage = new Intent(Intent.ACTION_PICK);
        iIntentImage.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        iIntentImage.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(iIntentImage, 2);
        
    }
    
    protected void trySendingResult() {
        try {
            sendResult();
        }
        catch (PetException iE) {
            Toast.makeText(getApplicationContext().getApplicationContext(), iE.getMessage(), Toast.LENGTH_LONG).show();
        }
        
    }
    
    protected void sendResult() throws PetException {
        ePet.setName(getName());
        ePet.setAge(getDate());
        ePet.setGender(getGender());
        ePet.setType(getType());
        ePet.setInfo(getExtra());
        ePet.setCode(getLeashCode());
        getHomeIntent().putExtra(C_KEY_PET, ePet);
        startActivity(getHomeIntent());
        
    }
    
    protected abstract void callTrySendingResult();
    
    protected abstract void initializePet();
    
    protected abstract void initializeKeys();
    
}

