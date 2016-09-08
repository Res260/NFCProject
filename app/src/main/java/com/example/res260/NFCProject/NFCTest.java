package com.example.res260.NFCProject;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class NFCTest extends AppCompatActivity {

    private NfcAdapter adapter;

    private NfcAdapter.ReaderCallback readCallback;

	private Loop loop;

    private Thread loopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("SALUT");

		this.loop = new Loop();
		this.loopThread = new Thread(this.loop);
		this.loopThread.start();

        this.readCallback = new ReadCallback(loop);

        adapter = NfcAdapter.getDefaultAdapter(this);
		System.out.println("SALUT2");
		adapter.enableReaderMode(this, this.readCallback, NfcAdapter.FLAG_READER_NFC_A, null);

		System.out.println("SALUT3");

    }

}
