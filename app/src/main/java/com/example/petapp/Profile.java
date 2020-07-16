package com.example.petapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class Profile extends Activity {
    
    private String C_KEY_NAME_LIST;
    CardView eBtnEdit;
    EditText eEdtEmail;
    EditText eEdtName;
    EditText eEdtPhone;
    EditText eEdtTwit;
    EditText eEdtInsta;
    EditText eEdtFace;
    boolean isEnabled = false;
    ImageView eImgEdit;
    SharedPreferences ePrefs;
    SharedPreferences.Editor eEditor;
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_profile);
        super.onCreate(savedInstanceState);
        LinearLayout eMap = findViewById(R.id.layout_bottom_home);
        LinearLayout eScan = findViewById(R.id.layout_bottom_member);
        LinearLayout eProfile = findViewById(R.id.layout_bottom_list);
        LinearLayout eLeave = findViewById(R.id.layout_bottom_chat);
        FloatingActionButton eBtnAdd = findViewById(R.id.FAB_Add);
        C_KEY_NAME_LIST = getString(R.string.KEY_NAMES_LIST);
        eBtnEdit = findViewById(R.id.Bt_Edit_User);
        eEdtEmail = findViewById(R.id.textView13);
        eEdtName = findViewById(R.id.textView7);
        eImgEdit = findViewById(R.id.Img_Edit);
        eEdtPhone = findViewById(R.id.editText12);
        eEdtTwit = findViewById(R.id.editText9);
        eEdtInsta = findViewById(R.id.xx);
        eEdtFace = findViewById(R.id.editText7);
        ePrefs = getPreferences(MODE_PRIVATE);
        eEditor = ePrefs.edit();
        String iName = ePrefs.getString("KEY_NAME", "");
        String iFace = ePrefs.getString("KEY_FACE", "");
        String iTwit = ePrefs.getString("KEY_TWIT", "");
        String iInsta = ePrefs.getString("KEY_INSTA", "");
        String iPhone = ePrefs.getString("KEY_PHONE", "");
        HashMap<Integer, String> x = (HashMap<Integer, String>) getIntent().getBundleExtra(C_KEY_NAME_LIST)
                                                                           .getSerializable(C_KEY_NAME_LIST);
        eEdtName.setText(iName.matches("") ? getString(R.string.TxtDefaultUsername) : iName);
        if (!iFace.matches(""))
            eEdtFace.setText(iFace);
        if (!iTwit.matches(""))
            eEdtTwit.setText(iTwit);
        if (!iInsta.matches(""))
            eEdtInsta.setText(iInsta);
        if (!iPhone.matches(""))
            eEdtPhone.setText(iPhone);
        eEditor = ePrefs.edit();
        //eEditor.putString(getString(R.string.KeyPetList), zJson);
        eEditor.apply();
        eBtnEdit.setOnClickListener(view -> {
            boolean enable = !isEnabled;
            eEdtName.setEnabled(enable);
            eEdtPhone.setEnabled(enable);
            eEdtTwit.setEnabled(enable);
            eEdtInsta.setEnabled(enable);
            eEdtFace.setEnabled(enable);
            if (!isEnabled) {
                isEnabled = true;
                eImgEdit.setImageResource(R.drawable.check);
            }
            else {
                isEnabled = false;
                eImgEdit.setImageResource(R.drawable.edit_white);
                eEditor.putString("KEY_NAME", eEdtName.getText().toString());
                eEditor.putString("KEY_PHONE", eEdtPhone.getText().toString());
                eEditor.putString("KEY_TWIT", eEdtTwit.getText().toString());
                eEditor.putString("KEY_INSTA", eEdtInsta.getText().toString());
                eEditor.putString("KEY_FACE", eEdtFace.getText().toString());
                eEditor.commit();
            }
        });
        eEdtTwit.setOnTouchListener((iView, iMotionEvent) -> {
            if (!eEdtTwit.getText().toString().startsWith("@"))
                eEdtTwit.getText().insert(0, "@");
            return false;
        });
        eEdtInsta.setOnTouchListener((view, motionEvent) -> {
            if (!eEdtInsta.getText().toString().startsWith("@"))
                eEdtInsta.getText().insert(0, "@");
            return false;
        });
        eEdtFace.setOnClickListener(view -> {
            
        });
        eEdtName.setOnClickListener(view -> {
            
        });
        eBtnAdd.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddPet.class)));
        eMap.setOnClickListener(view -> {
            Bundle iExtras = new Bundle();
            iExtras.putSerializable(C_KEY_NAME_LIST, x);
            Intent iIntent = new Intent(getApplicationContext(), MapActivity.class);
            iIntent.putExtra(C_KEY_NAME_LIST, iExtras);
            startActivity(iIntent);
        });
        eScan.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LeashCodeRead.class)));
        eLeave.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Enter.class)));
        eProfile.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NewHome.class)));
        
    }
    
}
