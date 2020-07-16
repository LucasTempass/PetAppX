package com.example.petapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

public class Fragment extends androidx.fragment.app.Fragment {
    
    private static String C_PET_KEY = "PET";
    private static String C_INDEX_KEY = "INDEX";
    private static String C_DISTANCE_KEY = "d";
    private TextView eName;
    private TextView eAdress;
    private TextView eTxtDistance;
    
    public Fragment() { }
    
    static Fragment newInstance(PetMissing iPetMissing, int iIndex, Location iLocation) {
        Fragment iFragmet = new Fragment();
        Bundle iArgs = new Bundle();
        iArgs.putSerializable(C_PET_KEY, iPetMissing);
        iArgs.putInt(C_INDEX_KEY, iIndex);
        Location iCurrentLocation = new Location("");
        iCurrentLocation.setLatitude(iPetMissing.getLat());
        iCurrentLocation.setLongitude(iPetMissing.getLon());
        iArgs.putFloat(C_DISTANCE_KEY, iLocation == null ? 0
                                                         : (iCurrentLocation.distanceTo(iLocation) / 1000));
        iFragmet.setArguments(iArgs);
        return iFragmet;
        
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View iView = inflater.inflate(R.layout.item_fragment, container, false);
        eName = iView.findViewById(R.id.head);
        eAdress = iView.findViewById(R.id.adress);
        eTxtDistance = iView.findViewById(R.id.Txt_Distance);
        View iBtnDialog = iView.findViewById(R.id.Bt_InfoDialog);
        iBtnDialog.setOnClickListener(v -> new InfoDialog(getActivity(), eAdress.getText().toString(), eName.getText().toString()).show());
        return iView;
        
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle iArgs = getArguments();
        PetMissing iMissingPet = (PetMissing) iArgs.getSerializable(C_PET_KEY);
        eName.setText(iMissingPet.getName());
        eTxtDistance.setText(generateFormattedDistance(iArgs.getFloat(C_DISTANCE_KEY), getString(R.string.MsgUnknownDistance)));
        eAdress.setText(generateFormattedAdress(getContext(), iMissingPet.getLat(), iMissingPet.getLon(), getString(R.string.MsgErrorLocation)));
        
    }
    
    private static String generateFormattedAdress(Context iContext, double iLat, double iLongit, String iErrorMessage) {
        Geocoder iGeocoder = new Geocoder(iContext, Locale.getDefault());
        try {
            //||| Old problem, may be still present |||
            //if (eAdress.getText().toString().matches(getString(R.string.MsgAddressNull)))
            //    eAdress.setText(R.string.MsgErrorLocation);
            Address iAddress = iGeocoder.getFromLocation(iLat, iLongit, 1).get(0);
            return iAddress.getThoroughfare() + " - " + iAddress.getSubThoroughfare();
        }
        catch (IOException e) {
            return iErrorMessage;
        }
        
    }
    
    private static String generateFormattedDistance(float iDistanceInFloat, String iErrorMessage) {
        if (iDistanceInFloat == 0)
            return iErrorMessage;
        DecimalFormat iFormat = new DecimalFormat("0.0");
        return "" + iFormat.format(iDistanceInFloat) + " km";
        
    }
    
}
