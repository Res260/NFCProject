package com.example.res260.NFCProject;

import android.media.MediaPlayer;

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

	private MediaPlayer player;
	private Boolean sonActive;

	private boolean continueLoop;

	private String playerName;
	private String playerTeam;

	private final long ACTIVATION_TIME = 5000;

	public Loop(InGame inGame) {
		this.inGame = inGame;
		this.timestampTagBegin = 0;
		this.timestampTagEnd = 0;
		this.continueLoop = true;
		this.sonActive = false;
		System.out.println("flag 1");
		this.player = MediaPlayer.create(inGame, R.raw.sound2);

		System.out.println("player initié");
	}


	public void run() {
		//Loop while we're in the activity.
		while(this.continueLoop) {
			if(this.timestampTagEnd < this.timestampTagBegin) {
				// Arming/disarming. Tag still on the phone. Perhaps has already been armed/disarmed.
				if(System.currentTimeMillis() - this.timestampTagBegin < ACTIVATION_TIME) {
					//Arming/disarming in progress.
					if (!this.sonActive){
						this.sonActive = true;
						this.player.start();
					}

					int progress = Math.round(((System.currentTimeMillis() - this.timestampTagBegin)
							/ (float) ACTIVATION_TIME) * (float) 1000);
					this.inGame.SetProgress(progress);
				} else {
					//Arming/disarming done. Sets the progression to 100%.
					this.inGame.SetProgress(1000);
				}
			} else {
				if (this.sonActive){
					try{
						this.player.stop();
						this.player.release();
						this.sonActive = false;
						this.player = MediaPlayer.create(this.inGame, R.raw.sound2);
					}catch (Exception e){
						System.out.println(e.getMessage());
					}

				}
				// Either has been armed/disarmed or failed attempt.
				if(this.timestampTagEnd - this.timestampTagBegin >= ACTIVATION_TIME) {
					//BOMB ARMED/DISARMED. Will be called one time when the tag is removed
					//after successful arming/disarming attempt.
					this.inGame.SetProgress(0); //Reset the progress bar.

					//Either arm or disarm.
					if(!this.inGame.isBombeArmee()) {
						if(this.playerTeam.equals(Loop.terrorists)) {
							this.inGame.ArmerBombe();
						}
					} else {
						if(this.playerTeam.equals(Loop.antiTerrorists)) {
							this.inGame.DesarmerBombe(this.playerName);
						}
					}

					//Resets timeStamps so it is not called more than once.
					this.timestampTagBegin = 0;
					this.timestampTagEnd = 0;
				} else {
					//FAILED TO ARM/DISARM BOMB OR nothing is happening/waiting.
					this.timestampTagBegin = 0;
					this.timestampTagEnd = 0;

					//Ensure progress is set to 0.
					this.inGame.SetProgress(0);
				}
			}
			try {
				//Reduces the CPU load.
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
