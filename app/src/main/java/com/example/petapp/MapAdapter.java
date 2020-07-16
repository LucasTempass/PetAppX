package com.example.petapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapAdapter extends FragmentPagerAdapter {
    
    private ArrayList<PetMissing> ePetsMissing;
    private Context eContext;
    private final float C_ZOOM = 16.5f;
    
    MapAdapter(FragmentManager fm, ArrayList<PetMissing> iPetMissings, Context iContext) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.ePetsMissing = iPetMissings;
        this.eContext = iContext;
        
    }
    
    @Override
    public int getCount() {
        return ePetsMissing.size();
    }
    
    public CameraPosition getCamPosition(int iPos) {
        return CameraPosition.fromLatLngZoom(new LatLng(ePetsMissing.get(iPos).getLat(), ePetsMissing.get(iPos).getLon()), C_ZOOM);
    }
    
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return com.example.petapp.Fragment.newInstance(ePetsMissing.get(position), position, getBestKnownLocation());
    }
    
    private Location getBestKnownLocation() {
        LocationManager iLocationManager = (LocationManager) eContext.getSystemService(LOCATION_SERVICE);
        List<String> iProviders = iLocationManager.getProviders(true);
        Location iLocation = null;
        Location iPlaceholder = null;
        for (String provider : iProviders) {
            if (ActivityCompat.checkSelfPermission(eContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat
                    .checkSelfPermission(eContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                iPlaceholder = iLocationManager.getLastKnownLocation(provider);
            if (iPlaceholder == null)
                continue;
            if (iLocation == null || iPlaceholder.getAccuracy() < iLocation.getAccuracy())
                iLocation = iPlaceholder;
        }
        return iLocation;
        
    }
    
}
