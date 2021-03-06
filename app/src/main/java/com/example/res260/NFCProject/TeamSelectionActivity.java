package com.example.res260.NFCProject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TeamSelectionActivity extends AppCompatActivity {

    // Widgets
    private Button buttonGo;

    private ListView listViewAnti, listViewTerr;
    private ArrayAdapter adapterListViewAnti, adapterListViewTerr;

    private List<String> nameListAnti = new LinkedList<>();
    private List<String> nameListTerr = new LinkedList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);

        sharedPreferences = getSharedPreferences("Joueurs", Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("Teamed", false)) {
            List<String> nameList = new ArrayList<>(sharedPreferences.getStringSet("Joueurs", new HashSet<String>()));
            Collections.shuffle(nameList);
            boolean lastWasAnti = false;
            for (String name : nameList) {
                if (lastWasAnti)
                    nameListTerr.add(name);
                else
                    nameListAnti.add(name);
                lastWasAnti = !lastWasAnti;
            }
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putStringSet("Anti", new HashSet<>(nameListAnti));
            ed.putStringSet("Terr", new HashSet<>(nameListTerr));
            ed.putBoolean("Teamed", true);
            ed.apply();
        } else {
            nameListAnti.addAll(sharedPreferences.getStringSet("Anti", new HashSet<String>()));
            nameListTerr.addAll(sharedPreferences.getStringSet("Terr", new HashSet<String>()));
        }

        // ListView settings ANTI TERRORIST
        adapterListViewAnti = new ArrayAdapter<>(this, R.layout.layout_listview_nom, nameListAnti);

        listViewAnti = (ListView) findViewById(R.id.listview_anti);
        listViewAnti.setAdapter(adapterListViewAnti);

        listViewAnti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = ((TextView)view).getText().toString();
                afficherJoueur(item, true);
            }
        });

        // ListView settings TERRORISTS
        adapterListViewTerr = new ArrayAdapter<>(this, R.layout.layout_listview_nom, nameListTerr);

        listViewTerr = (ListView) findViewById(R.id.listview_terro);
        listViewTerr.setAdapter(adapterListViewTerr);

        listViewTerr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = ((TextView)view).getText().toString();
                afficherJoueur(item, false);
            }
        });

        buttonGo = (Button) findViewById(R.id.button_go);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faireGO();
            }
        });
    }

    public void afficherJoueur(String nom, boolean isAnti) {
        Intent intent = new Intent(this, CheckInActivity.class);
        intent.putExtra("Nom", nom);
        intent.putExtra("isAnti", isAnti);
        startActivity(intent);
    }

    public void faireGO() {
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
    }
}
