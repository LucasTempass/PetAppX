package com.example.petapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class NewHome extends Activity {
    
    private int C_KEY_DELETE;
    private String C_KEY_PET;
    private String C_KEY_POSITION;
    private String C_KEY_BITMAP;
    private String C_KEY_ACTION;
    private int C_KEY_ADD;
    
    private ArrayList<Pet> ePets;
    private ArrayList<Evento> eEvents;
    
    private SharedPreferences.Editor eEditor;
    private SharedPreferences ePrefs;
    
    private FloatingActionButton eBtnAdd;
    private LinearLayout eBtnMap;
    private LinearLayout eBtnScan;
    private LinearLayout eBtnLeave;
    private LinearLayout eBtnProfile;
    private RecyclerView ePetRecyclerView;
    private ListView eEventsListView;
    private CardView eBtnEditPet;
    private CardView eBtnAddEvent;
    private LinearLayoutManager eLayoutManager;
    private PetsAdapter ePetAdapter;
    private EventAdapter eEventAdapter;
    private AddCalendarDialog eDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindViews();
        setUpAllListeners();
        initializeKeys();
        initializeListViews();
        handlePetAction();
        commitPets();
        commitEvents();
        
    }
    
    private Intent createEditIntent() {
        Intent iIntent = new Intent(getApplicationContext(), EditPet.class);
        Pet iPet = formatPetToEdit();
        if (iPet == null)
            return null;
        iIntent.putExtra(C_KEY_PET, iPet);
        iIntent.putExtra(C_KEY_POSITION, eLayoutManager.findLastVisibleItemPosition());
        return iIntent;
        
    }
    
    private Pet formatPetToEdit() {
        Pet iPet = null;
        try {
            iPet = ePets.get(eLayoutManager.findLastVisibleItemPosition());
            iPet.setImage(null);
        }
        catch (ArrayIndexOutOfBoundsException e) { }
        return iPet;
        
    }
    
    private void setUpAllListeners() {
        setUpBottomBarListeners();
        setUpButtonListeners();
        
    }
    
    private void setUpButtonListeners() {
        eBtnAddEvent.setOnClickListener(view -> startCalendarDialog());
        eBtnEditPet.setOnClickListener(view -> {
            Intent iIntent = createEditIntent();
            if (iIntent != null)
                startActivity(iIntent);
        });
        
    }
    
    private void startCalendarDialog() {
        eDialog = eDialog == null ? new AddCalendarDialog(NewHome.this) : eDialog;
        eDialog.show();
        eDialog.setOnDismissListener(dialog -> handleEventDialog(eDialog));
        
    }
    
    private void handleEventDialog(AddCalendarDialog iCalendarDialog) {
        if (iCalendarDialog.isDone()) {
            eEvents.add(getEventFromDialog(iCalendarDialog));
            eEventAdapter.notifyDataSetChanged();
            commitEvents();
        }
        eDialog = null;
        
    }
    
    private Evento getEventFromDialog(AddCalendarDialog iCalendarDialog) {
        Bundle iBundle = iCalendarDialog.getBundle();
        return (Evento) iBundle.getSerializable("CHANGED_EVENT");
        
    }
    
    private void initializePets() {
        ArrayList<Pet> iObject = getPetsFromPersistence();
        ePets = iObject != null ? iObject : new ArrayList<>();
        
    }
    
    private ArrayList<Pet> getPetsFromPersistence() {
        Gson iGsonPet = new Gson();
        String iJsonPet = ePrefs.getString(getString(R.string.KEY_PET_LIST), "");
        Type iTypePet = new TypeToken<ArrayList<Pet>>() { }.getType();
        return iGsonPet.fromJson(iJsonPet, iTypePet);
        
    }
    
    private void initializeEvents() {
        ArrayList<Evento> iObjectsEvents = getEventFromPersistence();
        eEvents = iObjectsEvents != null ? iObjectsEvents : new ArrayList<>();
        
    }
    
    private ArrayList<Evento> getEventFromPersistence() {
        Gson iGsonEvents = new Gson();
        String iJsonEvents = ePrefs.getString(getString(R.string.KEY_EVENT_LIST), "");
        Type iTypeEvent = new TypeToken<ArrayList<Evento>>() { }.getType();
        return iGsonEvents.fromJson(iJsonEvents, iTypeEvent);
        
    }
    
    private void initializeListViews() {
        createSharedPreferences();
        initializeEvents();
        initializePets();
        setUpListView();
        setUpRecyclerView();
        
    }
    
    private void createSharedPreferences() {
        ePrefs = getPreferences(MODE_PRIVATE);
        eEditor = ePrefs.edit();
        
    }
    
    private void setUpListView() {
        eEventAdapter = new EventAdapter(NewHome.this, eEvents);
        eEventsListView.setAdapter(eEventAdapter);
        
    }
    
    private void setUpRecyclerView() {
        createLayoutManager();
        ePetAdapter = new PetsAdapter(NewHome.this, ePets);
        ePetRecyclerView.setLayoutManager(eLayoutManager);
        ePetRecyclerView.setAdapter(ePetAdapter);
        
    }
    
    private void createLayoutManager() {
        eLayoutManager = new LinearLayoutManager(this);
        eLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        
    }
    
    private void handlePetAction() {
        int iKey = getIntent().getIntExtra(C_KEY_ACTION, -1);
        if (iKey == -1)
            return;
        int iPosition = getIntent().getIntExtra(C_KEY_POSITION, -1);
        if (iKey == C_KEY_ADD) {
            ePets.add(getPetFromIntent());
            ePetRecyclerView.scrollToPosition(ePets.size() - 1);
        }
        else if (iKey == C_KEY_DELETE)
            ePets.remove(iPosition);
        else
            ePets.set(iPosition, getPetFromIntent());
        ePetAdapter.notifyDataSetChanged();
        ePetRecyclerView.scrollToPosition(iPosition == -1 ? ePets.size() - 1 : iPosition);
        
    }
    
    private void bindViews() {
        eBtnMap = findViewById(R.id.layout_bottom_home);
        eBtnScan = findViewById(R.id.layout_bottom_member);
        eBtnProfile = findViewById(R.id.layout_bottom_list);
        eBtnLeave = findViewById(R.id.layout_bottom_chat);
        eBtnEditPet = findViewById(R.id.Bt_Edit_Pet);
        eBtnAdd = findViewById(R.id.FAB_Add);
        eBtnAddEvent = findViewById(R.id.Bt_Add_Eventx);
        ePetRecyclerView = findViewById(R.id.list);
        eEventsListView = findViewById(R.id.list2);
        
    }
    
    private void setUpBottomBarListeners() {
        eBtnAdd.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddPet.class)));
        eBtnScan.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LeashCodeRead.class)));
        eBtnLeave.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Enter.class)));
        eBtnMap.setOnClickListener(view -> generateNamesListIntent(MapActivity.class));
        eBtnProfile.setOnClickListener(view -> generateNamesListIntent(Profile.class));
        
    }
    
    private void initializeKeys() {
        C_KEY_PET = getString(R.string.KEY_PET);
        C_KEY_POSITION = getString(R.string.KEY_POSITION);
        C_KEY_BITMAP = getString(R.string.KEY_BITMAP);
        C_KEY_ACTION = getString(R.string.KEY_ACTION);
        C_KEY_DELETE = 2;
        C_KEY_ADD = 0;
        
    }
    
    private Pet getPetFromIntent() {
        Pet iPet = receivePetData();
        insertBitmapOnPet(iPet);
        return iPet;
        
    }
    
    private void commitEvents() {
        Gson iGsonEvent = new Gson();
        String iJsonEvent = iGsonEvent.toJson(eEvents);
        eEditor.putString(getString(R.string.KEY_EVENT_LIST), iJsonEvent);
        eEditor.commit();
        
    }
    
    private void commitPets() {
        Gson iGsonPet = new Gson();
        String iJsonPet = iGsonPet.toJson(ePets);
        eEditor.putString(getString(R.string.KEY_PET_LIST), iJsonPet);
        eEditor.commit();
        
    }
    
    private Pet receivePetData() {
        return getIntent().getParcelableExtra(C_KEY_PET);
    }
    
    private void insertBitmapOnPet(Pet iPet) {
        iPet.setImage(getBitmap(getIntent().getParcelableExtra(C_KEY_BITMAP)));
    }
    
    private HashMap<Integer, String> getNamesList(ArrayList<Pet> iPets) {
        HashMap<Integer, String> iNames = new HashMap<>();
        for (int i = 0; iPets.size() > i; i++)
            iNames.put(i, iPets.get(i).getName());
        return iNames;
        
    }
    
    private void generateNamesListIntent(Class iDestination) {
        Bundle iExtras = new Bundle();
        iExtras.putSerializable(getString(R.string.KEY_NAMES_LIST), getNamesList(ePets));
        Intent iIntent = new Intent(getApplicationContext(), iDestination);
        iIntent.putExtra(getString(R.string.KEY_NAMES_LIST), iExtras);
        startActivity(iIntent);
        
    }
    
    private Bitmap getBitmap(Uri iUri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), iUri);
        }
        catch (Exception iE) { return null; }
        
    }
    
}
