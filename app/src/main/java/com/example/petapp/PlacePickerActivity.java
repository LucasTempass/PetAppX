package com.example.petapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class PlacePickerActivity extends DialogTranslucent implements OnMapReadyCallback {
    
    private Activity eActivity;
    private String eTitle = "";
    private String eDescription = "";
    private HashMap<Integer, String> eNames;
    private ArrayList<String> ePetsNames;
    private List<Address> eAdresses = null;
    private BottomBar eBottomSheet;
    private ImageView eMarker;
    private GoogleMap eMap;
    private boolean eIsDone = false;
    private double C_DEFAULT_LAT = -29.759457;
    private double C_DEFAULT_LON = -51.146449;
    private float C_ZOOM = 14.0f;
    private String C_KEY_SPINNER;
    private String C_KEY_LAT;
    private String C_KEY_LONG;
    private String C_KEY_NAMES_LIST;
    private String C_KEY_ID;
    
    public PlacePickerActivity(@NonNull Activity eActivity, HashMap<Integer, String> iNames) {
        super(eActivity);
        this.eActivity = eActivity;
        this.eNames = iNames;
        mInitializeConstants();
        
    }
    
    private void mInitializeConstants() {
        C_KEY_SPINNER = eActivity.getString(R.string.KEY_SPINNER);
        C_KEY_LAT = eActivity.getString(R.string.KEY_LAT);
        C_KEY_LONG = eActivity.getString(R.string.KEY_LONGIT);
        C_KEY_NAMES_LIST = eActivity.getString(R.string.KEY_NAMES_LIST);
        C_KEY_ID = eActivity.getString(R.string.KEY_ID);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_placepicker);
        MapView iMapView = findViewById(R.id.mapview);
        iMapView.onCreate(onSaveInstanceState());
        iMapView.onResume();
        MapsInitializer.initialize(eActivity);
        iMapView.getMapAsync(this);
        ePetsNames = new ArrayList<>();
        addAllNames(eNames);
        ePetsNames.add(eActivity.getString(R.string.MsgSelectMissingPet));
        Spinner iSpinner = findViewById(R.id.spinner);
        SpinnerAdapter iSpinnerAdapter = new SpinnerAdapter(eActivity.getApplicationContext(), ePetsNames, R.layout.item_spinner);
        iSpinner.setAdapter(iSpinnerAdapter);
        iSpinner.setSelection(iSpinnerAdapter.getCount());
        eBottomSheet = findViewById(R.id.bottom_sheet);
        eMarker = findViewById(R.id.marker_image_view);
        FloatingActionButton iFab = findViewById(R.id.marker);
        iFab.setOnClickListener(v -> {
            if (iSpinner.getSelectedItem().toString().matches(eActivity.getString(R.string.MsgSelectMissingPet)))
                iSpinner.performClick();
            else {
                if (eAdresses != null) {
                    SharedPreferences.Editor editor = eActivity.getSharedPreferences(C_KEY_ID, MODE_PRIVATE).edit();
                    editor.putString(C_KEY_SPINNER, iSpinner.getSelectedItem().toString());
                    editor.putFloat(C_KEY_LAT, (float) eMap.getCameraPosition().target.latitude);
                    editor.putFloat(C_KEY_LONG, (float) eMap.getCameraPosition().target.longitude);
                    //editor.putInt("KEY_NUMBER", Integer.parseInt(addresses.get(0).getSubThoroughfare()));
                    editor.apply();
                    eIsDone = true;
                    dismiss();
                }
                else
                    Toast.makeText(eActivity, R.string.MsgInvalidAddress, Toast.LENGTH_LONG).show();
            }
        });
        
    }
    
    public boolean isReady() {
        return eIsDone;
    }
    
    private void getAddress() {
        try {
            Geocoder geoCoder = new Geocoder(eActivity, Locale.getDefault());
            eAdresses = geoCoder.getFromLocation(C_DEFAULT_LAT, C_DEFAULT_LON, 1);
            //if (eAdresses.size() != 0) {}
            String vAddress = eAdresses.get(0).getAddressLine(0);
            eDescription = vAddress.substring(vAddress.indexOf(",") + 2);
            eTitle = vAddress.substring(0, vAddress.indexOf(","));
            
        }
        catch (IOException ignored) {
            eDescription = "";
            eTitle = "";
            eAdresses = null;
        }
        
    }
    
    private void addAllNames(HashMap<Integer, String> iNames) {
        for (int i = 0; iNames.size() > i; i++)
            ePetsNames.add(iNames.get(i));
        
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        eMap = googleMap;
        eMap.setOnCameraMoveStartedListener(i -> {
            if (eMarker.getTranslationY() == 0f) {
                eMarker.animate().translationY(-75f).setInterpolator(new OvershootInterpolator()).setDuration(250).start();
                if (eBottomSheet.isVisible())
                    eBottomSheet.toggle();
            }
        });
        eMap.setOnCameraIdleListener(() -> {
            eMarker.animate().translationY(0f).setInterpolator(new OvershootInterpolator()).setDuration(250).start();
            eBottomSheet.load();
            LatLng latLng = eMap.getCameraPosition().target;
            C_DEFAULT_LAT = latLng.latitude;
            C_DEFAULT_LON = latLng.longitude;
            AsyncTask.execute(() -> {
                getAddress();
                eActivity.runOnUiThread(() -> eBottomSheet.setAdress(eTitle, eDescription));
            });
        });
        LocationServices.getFusedLocationProviderClient(eActivity).getLastLocation().addOnCompleteListener(task -> eMap
                .moveCamera(CameraUpdateFactory.newLatLngZoom(task.getResult() != null ? new LatLng(task.getResult().getLatitude(),
                        task.getResult().getLongitude()) : new LatLng(C_DEFAULT_LAT, C_DEFAULT_LON), C_ZOOM)));
        eMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(eActivity, R.raw.json));
        eMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
    }
    
}
