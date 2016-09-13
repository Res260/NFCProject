package com.example.res260.NFCProject;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class CheckInActivity extends AppCompatActivity {

    private String nom;
    private boolean isAnti;
    private Tag tag;

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
    public void onNewIntent(Intent intent) {

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            this.tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndefTag = Ndef.get(this.tag);
            try {
                if(ndefTag.isWritable()) {
                    NdefRecord record = NdefRecord.createTextRecord("en", "|" + this.nom+ "|" +
                            (this.isAnti ? Loop.antiTerrorists : Loop.terrorists));
                    NdefMessage message = new NdefMessage(record);
                    ndefTag.writeNdefMessage(message);
                    //this.activity.qqchoseSuccess;
                } else {
                    throw new IOException("non-writable");
                }
            } catch(IOException | FormatException e) {
                System.out.println("Erreur d'écriture");
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
