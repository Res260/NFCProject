package com.example.res260.NFCProject;

import android.app.Activity;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;

import java.io.IOException;

/**
 * Created by Res260 on 08/09/2016.
 */
public class CallBackCheckIn implements NfcAdapter.ReaderCallback {

	private final String playerName;
	private final String playerTeam;
	private Ndef ndefTag;
	private Activity activity;

	public CallBackCheckIn(Activity context, String name, String team) {
		this.activity = context;
		this.playerName = name;
		this.playerTeam = team;
	}

	@Override
	public void onTagDiscovered(Tag tag) {
		try {
			this.ndefTag = Ndef.get(tag);
			if(this.ndefTag.isWritable()) {
				NdefRecord record = NdefRecord.createTextRecord("en", "|" + this.playerName + "|" + this.playerTeam);
				NdefMessage message = new NdefMessage(record);
				this.ndefTag.writeNdefMessage(message);
				//this.activity.qqchoseSuccess;
			} else {
				throw new IOException("non-writable");
			}
		} catch(IOException | FormatException e) {
			System.out.println("RIP écriture");
			//this.activity.qqchoseRip;
		}
	}
}
