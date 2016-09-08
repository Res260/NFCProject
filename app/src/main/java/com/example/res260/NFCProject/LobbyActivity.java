package com.example.res260.NFCProject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {

    // Widgets
    private EditText editTextNom;
    private Button buttonAjouter, buttonSuivant;

    private ListView listViewNoms;
    private ArrayAdapter adapterListViewNoms;

    /**
     * Liste de noms de joueurs.
     */
    private List<String> nameList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        editTextNom = (EditText) findViewById(R.id.edittext_nom);

        // ListView settings
        adapterListViewNoms = new ArrayAdapter<>(this, R.layout.layout_listview_nom, nameList);

        listViewNoms = (ListView) findViewById(R.id.listview_noms);
        listViewNoms.setAdapter(adapterListViewNoms);
        registerForContextMenu(listViewNoms);

        // Ajouter settings
        buttonAjouter = (Button) findViewById(R.id.button_ajouter);
        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterJoueur();
            }
        });

        // Suivant settings
        buttonSuivant = (Button) findViewById(R.id.button_suivant);
        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suivant();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview_noms) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(nameList.get(info.position));
            menu.add("Supprimer");
            menu.add("Annuler");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle().equals("Supprimer"))
            nameList.remove(info.position);

        adapterListViewNoms.notifyDataSetChanged();
        return true;
    }

    public void ajouterJoueur() {
        String nom = editTextNom.getText().toString();
        if(!nom.isEmpty()) {
            nameList.add(nom);
            editTextNom.setText("");
            adapterListViewNoms.notifyDataSetChanged();
        }
    }

    public void suivant() {
        Intent intent = new Intent(this, TeamSelectionActivity.class);
        intent.putStringArrayListExtra("Joueurs", new ArrayList<>(nameList));
        startActivity(intent);
    }
}