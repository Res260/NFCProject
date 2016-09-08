package com.example.res260.NFCProject;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;

import java.io.IOException;

/**
 * Created by Res260 on 06/09/2016.
 * The callback class used by the OS when a NFC tag is scanned when the app is open.
 * The onTagDiscovered is called with said tag.
 */
public class CallbackInGame implements NfcAdapter.ReaderCallback {
	
	private Loop loop;

	public CallbackInGame(Loop loop) {
		this.loop = loop;
	}

	@Override
	public void onTagDiscovered(Tag tag) {

		this.loop.setTimestampTagBegin(System.currentTimeMillis());
		Ndef ndefTag = Ndef.get(tag);
		NdefMessage message = ndefTag.getCachedNdefMessage();
		NdefRecord record[] = message.getRecords();
		if(record.length > 0) {
			String content = new String(record[0].getPayload());
			String[] infos = content.split("\\|");
			if(infos.length == 3) {
				this.loop.setPlayerName(infos[1]);
				this.loop.setPlayerTeam(infos[2]);
			} else {
				System.out.println("Pas bon format :( :( :( :(");
			}
		}
		try {
			ndefTag.connect();

			boolean isConnected = ndefTag.isConnected();
			while(isConnected) {
				//System.out.println(isConnected);
				isConnected = ndefTag.isConnected();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("ERREUR NFC:");
					e.printStackTrace();
				}
			}

			ndefTag.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.loop.setTimestampTagEnd(System.currentTimeMillis());
	}

}
