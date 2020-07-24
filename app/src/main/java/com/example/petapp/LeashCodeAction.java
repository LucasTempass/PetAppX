package com.example.petapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class LeashCodeAction extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        QRCodeReaderView.OnQRCodeReadListener {
    
    private static final int C_CAMERA_PERMISSION = 0;
    protected View eView;
    protected ViewGroup eViewGroup;
    protected QRCodeReaderView eReaderView;
    protected ImageView eBtnFlash;
    protected ArrayList<String> eArrayList;
    protected ArrayList<String> eArrayList2;
    private ImageView eBtnReturnQR;
    private boolean isFlashOff = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    protected boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    
    protected void setUpAllListeners() {
        eBtnReturnQR.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewHome.class)));
        eBtnFlash.setOnClickListener(v -> {
            if (isFlashOff) {
                eReaderView.setTorchEnabled(true);
                isFlashOff = false;
            }
            else {
                eReaderView.setTorchEnabled(false);
                isFlashOff = true;
            }
        });
        
    }
    
    protected void bindViewGroup() {
        eViewGroup = findViewById(R.id.main_layout);
        
    }
    
    protected void startUp() {
        if (checkPermission())
            createView();
        else
            requestCameraPermission();
        
    }
    
    protected boolean isLeashCode() {
        //TODO||| starts with pet |||
        return true;
        
    }
    
    protected boolean isValidCode() {
        //TODO||| check cryptography |||
        return true;
        
    }
    
    protected void createView() {
        eView = getLayoutInflater().inflate(R.layout.activity_scanner, eViewGroup, true);
        bindViews();
        setUpAllListeners();
        setReaderViewProperties();
        
    }
    
    protected void bindViews() {
        eReaderView = eView.findViewById(R.id.qrdecoderview);
        eBtnFlash = eView.findViewById(R.id.flash);
        eBtnReturnQR = eView.findViewById(R.id.Bt_Return_Qr);
        
    }
    
    private void setReaderViewProperties() {
        eReaderView.setAutofocusInterval(1000L);
        eReaderView.setOnQRCodeReadListener(this);
        eReaderView.setBackCamera();
        eReaderView.startCamera();
        
    }
    
    protected void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
            Snackbar.make(eViewGroup, "\n" + getString(R.string.MsgCameraAcess), Snackbar.LENGTH_INDEFINITE).
                    setAction("OK", view -> ActivityCompat
                            .requestPermissions(LeashCodeAction.this, new String[]{Manifest.permission.CAMERA}, C_CAMERA_PERMISSION)).show();
        else {
            Toast.makeText(getApplicationContext(), R.string.MsgRequestingPermission, Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, C_CAMERA_PERMISSION);
        }
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (eReaderView != null)
            eReaderView.startCamera();
        
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (eReaderView != null)
            eReaderView.stopCamera();
        
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != C_CAMERA_PERMISSION) return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            createView();
        else Toast.makeText(getApplicationContext(), R.string.MsgPermissionDenied, Toast.LENGTH_LONG).show();
        
    }
    
    @Override
    public void onQRCodeRead(String text, PointF[] points) {}
    
}

