package com.example.res260.NFCProject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Alexandre on 2016-09-26.
 */
public class SoundService extends Service implements MediaPlayer.OnPreparedListener {

    private MediaPlayer player;
    public Boolean estPret = false;

    public void OnStartCommand(){
        this.player = this.player.create(this, R.raw.sound);
        this.player.setOnPreparedListener(this);
        this.player.prepareAsync(); // prepare async to not block main thread
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.estPret = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void restart(){
        if (this.player.isPlaying()){
            this.player.stop();
            this.player.prepareAsync();
        }

    }

    public void play(){
        if (!this.player.isPlaying()){
            this.player.start();
        }
    }
}
