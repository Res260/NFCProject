package com.example.res260.NFCProject;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class InGame extends AppCompatActivity {

    private final long terroristTime = 60 * 1000 * 5L;
    private final long antiTerroristTime = 60 * 1000 * 2L;
    public boolean bombeArmee = false;
    public TextView TextViewTime;
    private View NFCSpot;
    private CountDown timer;
    private NfcAdapter adapter;
    private NfcAdapter.ReaderCallback readCallback;
    private Loop loop;
    private Thread loopThread;
	private ProgressBar fuseProgressBar;

    private SharedPreferences sharedPreferences;
    private Set<String> nameSetAnti, nameSetTerr;

    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        // Set wakelock utils
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK
                                            | PowerManager.PARTIAL_WAKE_LOCK, "NFCProject");

        // Load names from shared pref
        sharedPreferences = getSharedPreferences("Joueurs", Context.MODE_PRIVATE);
        nameSetAnti = sharedPreferences.getStringSet("Anti", new HashSet<String>());
        nameSetTerr = sharedPreferences.getStringSet("Terr", new HashSet<String>());

        this.TextViewTime = (TextView) findViewById(R.id.textView);
		this.fuseProgressBar= (ProgressBar) findViewById(R.id.fuseProgressBar);
        this.NFCSpot = findViewById(R.id.NFCSpot);

        this.loop = new Loop(this);
        this.loopThread = new Thread(this.loop);
        this.loopThread.start();

        this.readCallback = new CallbackInGame(loop);

        adapter = NfcAdapter.getDefaultAdapter(this);
        adapter.enableReaderMode(this, this.readCallback, NfcAdapter.FLAG_READER_NFC_A, null);

        timer = new CountDown(this.terroristTime, 1000);
        timer.start();

    }

    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }

	public void SetProgress(final int perthousand) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				fuseProgressBar.setProgress(perthousand);
			}
		});
	}

    public void ArmerBombe() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!timer.isDone) {
					bombeArmee = true;
					timer.cancel();
					NFCSpot.setBackgroundResource(R.drawable.antiterrorist_cercle);
					timer = new CountDown(antiTerroristTime, 1000);
					timer.start();
				}
			}
		});
	}

    public int getPlayerTeam(String name) {
        if (this.nameSetAnti.contains(name)) {
            return 1;
        } else if (this.nameSetTerr.contains(name)) {
            return 2;
        } else {
			return 0;
		}
    }

    public void DesarmerBombe(final String savior) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(!timer.isDone) {
					timer.cancel();
					bombeArmee = false;
					String text = "Saved by: " + savior;
					TextViewTime.setText(text);
					TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
				}
			}
		});
    }

    public class CountDown extends CountDownTimer {

		public boolean isDone;

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
			this.isDone = false;
        }

        @Override
        public void onTick(long l) {

            long millis = l;
            String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            TextViewTime.setText(ms);
        }

        @Override
        public void onFinish() {
			TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
			if (bombeArmee) {
				TextViewTime.setText("Terrorists win!");
			} else {
				TextViewTime.setText("Counter-terrorists win!");
			}
			this.isDone = true;
        }
    }

    public boolean isBombeArmee() {
        return bombeArmee;
    }

    public void setBombeArmee(boolean bombeArmee) {
        this.bombeArmee = bombeArmee;
    }
}

