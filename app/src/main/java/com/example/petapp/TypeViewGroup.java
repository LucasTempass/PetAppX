package com.example.petapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TypeViewGroup {
    
    private Resources eResources;
    private Context context;
    private TextView textView;
    private ImageView imageView;
    private CardView cardView;
    private ConstraintLayout constraintLayout;
    private boolean eIsSelected;
    
    public TypeViewGroup(Context iContext, TextView iTextView, ImageView iImageView, CardView iCardView, ConstraintLayout iConstraintLayout) {
        textView = iTextView;
        imageView = iImageView;
        cardView = iCardView;
        constraintLayout = iConstraintLayout;
        context = iContext;
        //TODO||| Maybe switch context for resources|||
        eResources = context.getResources();
        eIsSelected = false;
        
    }
    
    public TextView getTextView() {
        return textView;
    }
    
    public ImageView getImageView() {
        return imageView;
    }
    
    public CardView getCardView() {
        return cardView;
    }
    
    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }
    
    public boolean isSelected() {
        return eIsSelected;
    }
    
    public void toggle() {
        int iColorPrimary = eIsSelected ? eResources.getColor(R.color.colorPrimaryDark) :
                            eResources.getColor(R.color.colorBackground);
        int iColorSecondary = eIsSelected ? eResources.getColor(R.color.colorBackground) :
                              eResources.getColor(R.color.colorPrimary);
        textView.setBackgroundTintList(ColorStateList.valueOf(iColorPrimary));
        textView.setTextColor(iColorSecondary);
        imageView.setBackgroundTintList(ColorStateList.valueOf(iColorSecondary));
        imageView.setColorFilter(iColorPrimary);
        cardView.setBackgroundTintList(ColorStateList.valueOf(iColorSecondary));
        constraintLayout.setBackgroundTintList(ColorStateList.valueOf(iColorPrimary));
        eIsSelected = !eIsSelected;
        
    }
    
}
