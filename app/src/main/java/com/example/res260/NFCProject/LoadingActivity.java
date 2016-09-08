package com.example.res260.NFCProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    private ArrayList<String> nameListAnti, nameListTerr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = getIntent();
        nameListAnti = intent.getStringArrayListExtra("Anti");
        nameListTerr = intent.getStringArrayListExtra("Terr");

        Button suivant = (Button) findViewById(R.id.button_suivant_loading);
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparationTerminee();
            }
        });
    }

    public void preparationTerminee() {
        Intent intent = new Intent(this, InGame.class);
        intent.putStringArrayListExtra("Anti", nameListAnti);
        intent.putStringArrayListExtra("Terr", nameListTerr);
        startActivity(intent);
    }
}
