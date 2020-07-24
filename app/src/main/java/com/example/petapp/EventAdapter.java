package com.example.petapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter implements View.OnClickListener {
    
    private Activity eContext;
    private ArrayList<Event> eItens;
    private Bundle eBundle;
    private EditCalendarDialog eEditDialog;
    private SharedPreferences.Editor eEditor;
    private String eJson;
    private Gson eGson;
    
    EventAdapter(Activity iContext, ArrayList<Event> iItens) {
        this.eContext = iContext;
        this.eItens = iItens;
        eBundle = new Bundle();
        
    }
    
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    
    @Override
    public int getCount() {
        return eItens.size();
    }
    
    @Override
    public Object getItem(int position) {
        return eItens.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View iView, ViewGroup parent) {
        iView = LayoutInflater.from(eContext).inflate(R.layout.item_event, parent, false);
        TextView iTxt_Alarm = iView.findViewById(R.id.Txt_Alarm);
        TextView iTxt_Text = iView.findViewById(R.id.txt_name_info);
        TextView iTxt_Time = iView.findViewById(R.id.Txt_Time);
        ImageView iBt_Dialog = iView.findViewById(R.id.Bt_Ellipses);
        iTxt_Text.setText(eItens.get(position).getName());
        iTxt_Time.setText(eItens.get(position).getTime());
        iTxt_Alarm.setText(eItens.get(position).getDay() + "/" + eItens.get(position).getMonth());
        iBt_Dialog.setTag(position);
        iBt_Dialog.setOnClickListener(this);
        return iView;
        
    }
    
    @Override
    public void onClick(View iView) {
        Event iEvent = eItens.get((Integer) iView.getTag());
        SharedPreferences iPrefs = eContext.getPreferences(Context.MODE_PRIVATE);
        eEditor = iPrefs.edit();
        eBundle.putSerializable("EVENT", iEvent);
        eEditDialog = eEditDialog == null ? new EditCalendarDialog(eContext, eBundle) : eEditDialog;
        eEditDialog.show();
        eEditDialog.setOnDismissListener(dialog -> {
            if (eEditDialog.isDone()) {
                Bundle iBundle = eEditDialog.getBundle();
                eItens.set((Integer) iView.getTag(), (Event) iBundle.getSerializable("CHANGED_EVENT"));
            }
            else if (eEditDialog.wasDeleted())
                eItens.remove(((Integer) iView.getTag()).intValue());
            eGson = new Gson();
            eJson = eGson.toJson(eItens);
            eEditor.putString(eContext.getString(R.string.KEY_EVENT_LIST), eJson);
            eEditor.apply();
            notifyDataSetChanged();
            eEditDialog = null;
        });
        
    }
    
}