package com.example.petapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, android.location.LocationListener {
    
    private static final int C_REQUEST_CODE = 21;
    private static final int C_REQUEST_RES = 42;
    private final int C_DISPLACEMENT_METERS = 40;
    private final int C_INTERVAL_MILLIS = 20000;
    private String C_KEY_SPINNER;
    private String C_KEY_LAT;
    private String C_KEY_LONG;
    private String C_KEY_NAMES_LIST;
    private String C_KEY_ID;
    private List<Geofence> eGeofenceList;
    private PlacePickerActivity eDialog;
    private ArrayList<PetMissing> ePetMissings;
    private MapAdapter eMapAdapter;
    private GoogleApiClient eClientGoogleApi;
    private Location eLastLocation;
    private MapViewPager eMvp;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment vMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        vMapFragment.getMapAsync(this);
        C_KEY_SPINNER = getString(R.string.KEY_SPINNER);
        C_KEY_LAT = getString(R.string.KEY_LAT);
        C_KEY_LONG = getString(R.string.KEY_LONGIT);
        C_KEY_NAMES_LIST = getString(R.string.KEY_NAMES_LIST);
        C_KEY_ID = getString(R.string.KEY_ID);
        eGeofenceList = new ArrayList<>();
        ViewPager iViewPager = findViewById(R.id.mapviewpager);
        GeofencingClient iClientGeoFence = LocationServices.getGeofencingClient(getApplicationContext());
        setLocation();
        ePetMissings = new ArrayList<>();
        //TODO IF EMPTY ADD DEFAULT EXAMPLE
        ePetMissings.add(new PetMissing("Pet", 1, -29.7693232, -51.13459619));
        //FirebaseFirestore iFirebase = FirebaseFirestore.getInstance();
        //CollectionReference iPetsReference = iFirebase.collection(getString(R.string.KeyCollectionPath));
        //iPetsReference.get().addOnCompleteListener(task -> {
        //    for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
        //        PetMissing iPet = snapshot.toObject(PetMissing.class);
        //        iPet.setID(snapshot.getId());
        //        ePetMissings.add(iPet);
        //    }
        //    eMapAdapter = new MapAdapter(getSupportFragmentManager(), ePetMissings, getApplicationContext());
        //    eMvp = new MapViewPager(vMapFragment, iViewPager, eMapAdapter, getApplicationContext());
        //});
        eMapAdapter = new MapAdapter(getSupportFragmentManager(), ePetMissings, getApplicationContext());
        eMvp = new MapViewPager(vMapFragment, iViewPager, eMapAdapter, getApplicationContext());
        eGeofenceList.add(createGeofenceDwell("Id", -29.7693232, -51.13459619, 500, 1000000, 10));
        iClientGeoFence.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
        ImageView iBtnReturnMap = findViewById(R.id.bt_return_map);
        iBtnReturnMap.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewHome.class)));
        ImageView iBtnAdd = findViewById(R.id.bt_add_missing);
        iBtnAdd.setOnClickListener(v -> LocationServices.getFusedLocationProviderClient(getApplicationContext()).
                getLastLocation().addOnCompleteListener(task -> {
            eDialog = new PlacePickerActivity(MapActivity.this, (HashMap<Integer, String>) getIntent()
                    .getBundleExtra(C_KEY_NAMES_LIST).getSerializable(C_KEY_NAMES_LIST));
            eDialog.show();
            eDialog.setOnDismissListener(dialog -> {
                //TODO||| Add to Firebase |||
                if (task.getResult() == null)
                    Toast.makeText(getApplicationContext(), R.string.MsgTurnOnGPS, Toast.LENGTH_LONG).show();
                if (eDialog.isReady())
                    ePetMissings.add(new PetMissing(getSharedPreferences(C_KEY_ID, MODE_PRIVATE)
                            .getString(C_KEY_SPINNER, "Hey"), 1,
                            getSharedPreferences(C_KEY_ID, MODE_PRIVATE)
                                    .getFloat(C_KEY_LAT, 2),
                            getSharedPreferences(C_KEY_ID, MODE_PRIVATE)
                                    .getFloat(C_KEY_LONG, 2)));
                eMapAdapter.notifyDataSetChanged();
                eMvp.addAllMarkers();
            });
        }));
        
    }
    
    private Geofence createGeofenceDwell(String iId, double iLat, double iLng, int iRadius, long iDuration, int iDelay) {
        return new Geofence.Builder().setRequestId(iId).setCircularRegion(iLat, iLng, iRadius).
                setExpirationDuration(iDuration).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).build();
        
    }
    
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder iBuilder = new GeofencingRequest.Builder();
        iBuilder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        iBuilder.addGeofences(eGeofenceList);
        return iBuilder.build();
        
    }
    
    private PendingIntent getGeofencePendingIntent() {
        int iRandomInt = new Random().nextInt(61) + 20;
        return PendingIntent.getService(getApplicationContext(),
                iRandomInt, new Intent(getApplicationContext(), GeofenceTransitionsIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        
    }
    
    private void setLocation() {
        if (!hasPermissions())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, C_REQUEST_CODE);
        else if (checkPlayServices())
            buildGoogleApi();
        
    }
    
    private boolean hasPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        
    }
    
    private void isFirstTry() {
        if (!hasPermissions())
            return;
        if (eLastLocation != null) {
            eLastLocation.getLatitude();
            eLastLocation.getLongitude();
        }
        
    }
    
    //TODO||| Change Too Repetitive|||
    private void displayLocation() {
        if (!hasPermissions())
            return;
        if (eLastLocation == null)
            Toast.makeText(getApplicationContext(), "\n" + getString(R.string.MsgLocationNotFound), Toast.LENGTH_SHORT).show();
        
    }
    
    @SuppressLint("MissingPermission")
    private void requestLocation() {
        if (!hasPermissions())
            return;
        LocationManager iLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria iCriteria = new Criteria();
        iCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        iLocationManager.requestLocationUpdates(iLocationManager.getBestProvider(iCriteria, false), C_INTERVAL_MILLIS, C_DISPLACEMENT_METERS, this);
        
    }
    
    private void buildGoogleApi() {
        eClientGoogleApi = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        eClientGoogleApi.connect();
        
    }
    
    //TODO||| Update Deprecated Methods |||
    private boolean checkPlayServices() {
        int iResultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (iResultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(iResultCode))
                GooglePlayServicesUtil.getErrorDialog(iResultCode, this, C_REQUEST_RES).show();
            else {
                Toast.makeText(this, R.string.MsgDeviceNotSupported, Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
        
    }
    
    @Override
    protected void onActivityResult(int iCode, int iResult, @Nullable Intent iData) {
        if (iCode == 100)
            if (iResult == Activity.RESULT_OK && iData != null) {
                //getSharedPreferences("ID", MODE_PRIVATE).getString("Name", "NONE");
                //AdressData addressData = data.getParcelableExtra("ADDRESS_INTENT");
                ////eDialog.setText(addressData.getAddressList().get(0).getThoroughfare());
                //SharedPreferences.Editor editor = getSharedPreferences("KEY_LOCATION", MODE_PRIVATE).edit();
                //editor.putBoolean("SELECTED", true).apply();
                //Lat = addressData.getLatitude();
                //Lon = addressData.getLongitude();
                //editor.apply();
                //Toast.makeText(getApplicationContext(), addressData.getAddressList().get(0).getThoroughfare(),Toast.LENGTH_LONG ).show();
                //eDialog.dismiss();
                //addressData.getAddressList().get(0).getCountryName();
            }
            else super.onActivityResult(iCode, iResult, iData);
        
    }
    
    @Override
    public void onMapReady(GoogleMap iMap) {
        iMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.json));
        iMap.getUiSettings().setMapToolbarEnabled(false);
        
    }
    
    @Override
    public void onConnected(@Nullable Bundle iBundle) {
        requestLocation();
        final Handler handler = new Handler();
        handler.postDelayed(this::isFirstTry, 3000);
        
    }
    
    @Override
    public void onConnectionSuspended(int i) {
        eClientGoogleApi.connect();
    }
    
    @Override
    public void onRequestPermissionsResult(int iCode, @NonNull String[] iPermissions, @NonNull int[] iGrantResults) {
        if (iCode == C_REQUEST_CODE && iGrantResults.length > 0 && iGrantResults[0] == PackageManager.PERMISSION_GRANTED && checkPlayServices())
            buildGoogleApi();
        
    }
    
    @Override
    public void onLocationChanged(Location iLocation) {
        eLastLocation = iLocation;
        displayLocation();
        
    }
    
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult iResult) {
    }
    
    @Override
    public void onStatusChanged(String iProvider, int iStatus, Bundle iBundle) {
        
    }
    
    @Override
    public void onProviderEnabled(String iProvider) {
        
    }
    
    @Override
    public void onProviderDisabled(String iProvider) {
        
    }
    
}
