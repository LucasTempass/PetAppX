package com.example.petapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;

public class MapViewPager extends FrameLayout implements OnMapReadyCallback {
    
    protected List<Marker> eMarkers;
    private GoogleMap eMap;
    private ViewPager eViewPager;
    private MapAdapter eAdapter;
    
    public MapViewPager(Context iContext) {
        super(iContext);
    }
    
    public MapViewPager(Context iContext, AttributeSet iAttrs) {
        super(iContext, iAttrs);
        LayoutInflater.from(iContext).inflate(R.layout.view_mvp, this);
        
    }
    
    public MapViewPager(SupportMapFragment iMapFragment, ViewPager iViewPager, MapAdapter iAdapter, Context iContext) {
        super(iContext);
        eViewPager = iViewPager;
        eAdapter = iAdapter;
        iMapFragment.getMapAsync(this);
        
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        eMap = googleMap;
        eViewPager.setAdapter(eAdapter);
        eViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            
            @Override
            public void onPageSelected(int iPos) {
                moveTo(iPos);
            }
            
        });
        addAllMarkers();
        moveTo(eViewPager.getCurrentItem());
        
    }
    
    public void addAllMarkers() {
        eMap.clear();
        eMarkers = new LinkedList<>();
        for (int i = 0; i < eAdapter.getCount(); i++) {
            Marker iMarker = eMap.addMarker(createOptions(eAdapter.getCamPosition(i)));
            iMarker.hideInfoWindow();
            eMarkers.add(iMarker);
        }
        
    }
    
    private void moveTo(int iPosition) {
        eMap.animateCamera(CameraUpdateFactory.newCameraPosition(eAdapter.getCamPosition(iPosition)));
    }
    
    private MarkerOptions createOptions(CameraPosition iCamPosition) {
        if (iCamPosition == null || iCamPosition.target == null)
            return null;
        Drawable iDrawable = ContextCompat.getDrawable(getContext(), R.drawable.marker_green);
        Bitmap iIcon = Bitmap.createBitmap(100, 126, Bitmap.Config.ARGB_8888);
        Canvas iCanvas = new Canvas(iIcon);
        iDrawable.setBounds(0, 0, iCanvas.getWidth(), iCanvas.getHeight());
        iDrawable.draw(iCanvas);
        return new MarkerOptions().position(new LatLng(iCamPosition.target.latitude, iCamPosition.target.longitude))
                                  .icon(BitmapDescriptorFactory.fromBitmap(iIcon));
        
    }
    
}
