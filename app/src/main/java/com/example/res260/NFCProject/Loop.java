package com.example.res260.NFCProject;

import android.app.Activity;

/**
 * Created by Res260 on 07/09/2016.
 * The thread loop to check the bomb status according to input/output from ReadCallBack:
 * -When a tag was initially read
 * -When said tag was removed from the phone.
 */
public class Loop implements Runnable {

	public final static String terrorists = "Terroristes";
	public final static String antiTerrorists = "Anti-terroristes";

	private final InGame inGame;

	private long timestampTagBegin;

	private long timestampTagEnd;

	private boolean continueLoop;

	private String playerName;
	private String playerTeam;

	private final long ACTIVATION_TIME = 5000;

	public Loop(InGame inGame) {
		this.inGame = inGame;
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
					int progress = Math.round(((System.currentTimeMillis() - this.timestampTagBegin)
							/ (float) ACTIVATION_TIME) * (float) 1000);
					this.inGame.SetProgress(progress);
				} else {
					System.out.println("TRIGGER DISARMED");
					this.inGame.SetProgress(1000);
				}
			} else {
				// Either has been armed/disarmed or failed attempt.
				if(this.timestampTagEnd - this.timestampTagBegin >= ACTIVATION_TIME) {
					//BOMB ARMED/DISARMED
					System.out.println("BOMB ARMED/DISARMED");
					this.inGame.SetProgress(0);
					if(!this.inGame.isBombeArmee()) {
						if(this.playerTeam.equals(Loop.terrorists)) {
							this.inGame.ArmerBombe();
						}
					} else {
						if(this.playerTeam.equals(Loop.antiTerrorists)) {
							this.inGame.DesarmerBombe(this.playerName);
						}
					}
					this.timestampTagBegin = 0;
					this.timestampTagEnd = 0;
				} else {
					//FAILED TO ARM/DISARM BOMB
					this.timestampTagBegin = 0;
					this.timestampTagEnd = 0;
					System.out.println("FAILED TO ARM/DISARM BOMB");
					this.inGame.SetProgress(0);
				}
			}
			try {
				Thread.sleep(50);
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

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerTeam() {
		return playerTeam;
	}

	public void setPlayerTeam(String playerTeam) {
		this.playerTeam = playerTeam;
	}
}
