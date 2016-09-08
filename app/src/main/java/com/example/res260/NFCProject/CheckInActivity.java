package com.example.res260.NFCProject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CheckInActivity extends AppCompatActivity {

    private String nom;
    private boolean isAnti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Intent intent = getIntent();
        nom = intent.getStringExtra("Nom");
        isAnti = intent.getBooleanExtra("isAnti", true);

        if (isAnti) {
            ((TextView) findViewById(R.id.textview_team)).setText(R.string.text_under_anti_terrorists);
            findViewById(R.id.NFCSpotTerro).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) findViewById(R.id.textview_team)).setText(R.string.text_under_terrorists);
            findViewById(R.id.NFCSpotAntiTerro).setVisibility(View.INVISIBLE);
        }

        ((TextView) findViewById(R.id.textview_checkin_name)).setText(nom);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
