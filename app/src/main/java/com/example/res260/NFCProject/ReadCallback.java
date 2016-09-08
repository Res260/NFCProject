package com.example.res260.NFCProject;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;

import java.io.IOException;

/**
 * Created by Res260 on 06/09/2016.
 * The callback class used by the OS when a NFC tag is scanned when the app is open.
 * The onTagDiscovered is called with said tag.
 */
public class ReadCallback implements NfcAdapter.ReaderCallback {
	
	private Loop loop;

	public ReadCallback(Loop loop) {
		this.loop = loop;
	}

	@Override
	public void onTagDiscovered(Tag tag) {

		this.loop.setTimestampTagBegin(System.currentTimeMillis());

		Ndef ndefTag = Ndef.get(tag);
		try {
			ndefTag.connect();

			boolean isConnected = true;
			while(isConnected) {
				System.out.println(isConnected);
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
