package com.example.res260.NFCProject;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class CheckInActivity extends AppCompatActivity {

    private String nom;
    private boolean isAnti;
    private Tag tag;

	private IntentFilter[] intentFiltersArray;
	private String[][] techListsArray;
	private PendingIntent pendingIntent;
	private NfcAdapter nfcAdapter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		System.out.println("HIHI");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

		this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		this.pendingIntent = PendingIntent.getActivity(
				this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
		}
		catch (IntentFilter.MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		this.intentFiltersArray = new IntentFilter[] {ndef, };
		this.techListsArray = new String[][] { new String[] { NfcA.class.getName() } };

        Intent intent = getIntent();
        this.nom = intent.getStringExtra("Nom");
        this.isAnti = intent.getBooleanExtra("isAnti", true);

        if (isAnti) {
            ((TextView) findViewById(R.id.textview_team)).setText(R.string.text_under_anti_terrorists);
            findViewById(R.id.NFCSpotTerro).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) findViewById(R.id.textview_team)).setText(R.string.text_under_terrorists);
            findViewById(R.id.NFCSpotAntiTerro).setVisibility(View.INVISIBLE);
        }

        ((TextView) findViewById(R.id.textview_checkin_name)).setText(nom);

		System.out.println("HIHI2");
    }

	@Override
	public void onPause() {
		System.out.println("HIHI4");
		super.onPause();
		this.nfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	public void onResume() {
		System.out.println("HIHI3");
		super.onResume();
		this.nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
	}

	@Override
	public void onNewIntent(Intent intent) {
		System.out.println("NEW INTENT");
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Ndef ndefTag = Ndef.get(tagFromIntent);
		if(ndefTag.isWritable()) {
			String text = "|" + this.nom + "|" + (this.isAnti
					? getResources().getString(R.string.text_anti_terro)
					: getResources().getString(R.string.text_terrorists));
			try {
				NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, null, null, text.getBytes());
				NdefMessage ndefMessage = new NdefMessage(ndefRecord);
				ndefTag.connect();
				ndefTag.writeNdefMessage(ndefMessage);
				System.out.println("WRITE COMPLETE");
				Toast toast = Toast.makeText(this, "Écriture réussie!", Toast.LENGTH_LONG);
				toast.show();
			} catch(Exception e) {
				System.out.println(e.toString());
				Toast toast = Toast.makeText(this, "Erreur lors de l'écriture.", Toast.LENGTH_LONG);
				toast.show();
			}
		}
		//do something with tagFromIntent
	}

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
