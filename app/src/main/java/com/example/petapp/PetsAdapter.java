package com.example.petapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetViewHolder> {
    
    private Activity eContext;
    private ArrayList<Pet> ePets;
    private QRDialog eDialog;
    
    PetsAdapter(Activity iContext, ArrayList<Pet> iMissingPets) {
        this.eContext = iContext;
        this.ePets = iMissingPets;
        
    }
    
    @Override
    public void onBindViewHolder(PetViewHolder iHolder, int iPosition) {
        Pet iPetMissing = ePets.get(iPosition);
        if (iPetMissing.getType() != 0)
            iHolder.eImgPet.setImageDrawable(eContext.getDrawable(iPetMissing.getType() == 1 ? R.drawable.cat : R.drawable.question));
        if (!iPetMissing.isGender())
            iHolder.eImgSex.setImageDrawable(eContext.getDrawable(R.drawable.male));
        iHolder.eTxtName.setText(iPetMissing.getName());
        iHolder.eTxtAge.setText(generateAgeFormatted(iPetMissing.getAge()));
        iHolder.eBtnScan.setOnClickListener(v -> {
            eDialog = eDialog == null ? new QRDialog(eContext) : eDialog;
            eDialog.show();
        });
        
    }
    
    @Override
    public int getItemCount() {
        return ePets.size();
    }
    
    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iView = LayoutInflater.from(eContext).inflate(R.layout.item_pet_home, parent, false);
        return new PetViewHolder(iView);
        
    }
    
    //TODO||| REMAKE |||
    private String generateAgeFormatted(String iDateString) {
        Date iDate;
        try {
            iDate = new SimpleDateFormat("dd/MM/yyyy").parse(iDateString);
        }
        catch (ParseException iE) {
            iDate = new Date();
        }
        Calendar iBirthDayCalendar = new GregorianCalendar();
        Calendar iTodayCalendar = new GregorianCalendar();
        iBirthDayCalendar.setTime(iDate);
        iTodayCalendar.setTime(new Date());
        int iIntraYears = iTodayCalendar.get(Calendar.YEAR) - iBirthDayCalendar.get(Calendar.YEAR);
        return iIntraYears == 0 ? iIntraYears * 12 + (iTodayCalendar.get(Calendar.MONTH) - iBirthDayCalendar.get(Calendar.MONTH)) + " " + eContext
                .getString(R.string.TxtMonths) :
               iIntraYears < 0 ? eContext.getString(R.string.Txt0Years) : (long) iIntraYears + " " + eContext.getString(R.string.TxtYears);
        
    }
    
    static class PetViewHolder extends RecyclerView.ViewHolder {
        
        ImageView eImgPet;
        ImageView eImgSex;
        TextView eTxtName;
        TextView eTxtAge;
        ImageView eBtnScan;
        
        PetViewHolder(View iView) {
            super(iView);
            eImgPet = iView.findViewById(R.id.Pet_Icon);
            eImgSex = iView.findViewById(R.id.Icon_Sex);
            eTxtName = iView.findViewById(R.id.Txt_Name);
            eTxtAge = iView.findViewById(R.id.Txt_Age);
            eBtnScan = iView.findViewById(R.id.Bt_Qr);
            
        }
        
    }
    
}