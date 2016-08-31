package com.example.res260.NFCProject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {

    // Widgets
    private EditText editTextNom;
    private Button buttonAjouter;

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

        listViewNoms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = ((TextView)view).getText().toString();
                System.out.println(item);
            }
        });

        // Ajouter settings
        buttonAjouter = (Button) findViewById(R.id.button_ajouter);
        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterJoueur();
            }
        });
    }

    public void ajouterJoueur() {
        String nom = editTextNom.getText().toString();
        if(!nom.isEmpty()) {
            nameList.add(nom);
            editTextNom.setText("");
            adapterListViewNoms.notifyDataSetChanged();
        }
    }
}
