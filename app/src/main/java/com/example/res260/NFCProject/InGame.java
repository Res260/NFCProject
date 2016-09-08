package com.example.res260.NFCProject;

import android.nfc.NfcAdapter;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class InGame extends AppCompatActivity {

    private final long terroristTime = 60 * 1000 * 5L;
    private final long antiTerroristTime = 60 * 1000 * 2L;
    private boolean bombeArmee = false;
    public TextView TextViewTime;
    private View NFCSpot;
    private CountDown timer;
    private NfcAdapter adapter;
    private NfcAdapter.ReaderCallback readCallback;
    private Loop loop;
    private Thread loopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        this.TextViewTime = (TextView) findViewById(R.id.textView);
        this.NFCSpot = (View) findViewById(R.id.NFCSpot);

        this.loop = new Loop(this);
        this.loopThread = new Thread(this.loop);
        this.loopThread.start();

        this.readCallback = new CallbackInGame(loop);

        adapter = NfcAdapter.getDefaultAdapter(this);
        adapter.enableReaderMode(this, this.readCallback, NfcAdapter.FLAG_READER_NFC_A, null);

        timer = new CountDown(this.terroristTime, 1000);
        timer.start();

    }


    public void ArmerBombe(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bombeArmee = true;
				timer.cancel();
				NFCSpot.setBackgroundResource(R.drawable.antiterrorist_cercle);
				timer = new CountDown(antiTerroristTime, 1000);
				timer.start();
			}
		});
    }

    public void DesarmerBombe(final String savior) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				timer.cancel();
				bombeArmee = false;
				String text = "Saved by: " + savior;
				TextViewTime.setText(text);
			}
		});
    }

    public class CountDown extends CountDownTimer {

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

            long millis = l;
            String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(ms);
            TextViewTime.setText(ms);
        }

        @Override
        public void onFinish() {
            TextViewTime.setText("Game Over");
        }
    }

    public boolean isBombeArmee() {
        return bombeArmee;
    }

    public void setBombeArmee(boolean bombeArmee) {
        this.bombeArmee = bombeArmee;
    }
}

