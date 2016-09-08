package com.example.res260.NFCProject;

import android.nfc.NfcAdapter;
import android.nfc.Tag;

/**
 * Created by Res260 on 07/09/2016.
 * The thread loop to check the bomb status according to input/output from ReadCallBack:
 * -When a tag was initialy read
 * -When said tag was removed from the phone.
 */
public class Loop implements Runnable {

	private long timestampTagBegin;

	private long timestampTagEnd;

	private boolean continueLoop;

	private final long ACTIVATION_TIME = 5000;

	public Loop() {
		this.timestampTagBegin = 0;
		this.timestampTagEnd = 0;
		this.continueLoop = true;
	}


	public void run() {
		while(this.continueLoop) {
			if(this.timestampTagEnd < this.timestampTagBegin) {
				// Arming/disarming. Tag still on the phone. Perhaps has already been armed/disarmed.
				if(System.currentTimeMillis() - this.timestampTagBegin < ACTIVATION_TIME) {
					System.out.println("ARMING/DISARMING");
				} else {
					System.out.println("TRIGGER DISARMED");

				}
			} else {
				// Either has been armed/disarmed or failed attempt.
				if(this.timestampTagEnd - this.timestampTagBegin >= ACTIVATION_TIME) {
					//BOMB ARMED/DISARMED
					System.out.println("BOMB ARMED/DISARMED");
					this.timestampTagBegin = 0;
					this.timestampTagEnd = 0;
				} else {
					//FAILED TO ARM/DISARM BOMB
					this.timestampTagBegin = 0;
					this.timestampTagEnd = 0;
					System.out.println("FAILED TO ARM/DISARM BOMB");
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public long getTimestampTagBegin() {
		return timestampTagBegin;
	}

	public long getTimestampTagEnd() {
		return timestampTagEnd;
	}

	public void setTimestampTagEnd(long timestampTagEnd) {
		this.timestampTagEnd = timestampTagEnd;
	}

	public void setTimestampTagBegin(long timestampTagBegin) {
		this.timestampTagBegin = timestampTagBegin;
	}
}
