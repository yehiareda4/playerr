package com.aait.player;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    MediaPlayer player = null;
    SensorManager sensorManager = null;
    Sensor accelerometerSensor = null;
    Boolean accelerometerPresent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPlayer();
        initSensor();
    }

    //Start Run Music File From Resources
    private void initPlayer() {
        int resId = getResources().getIdentifier(String.valueOf(R.raw.adu), "raw", getPackageName());
        player = new MediaPlayer();
        player = MediaPlayer.create(this, resId);
        player.start();
    }

    //Get Instance Of accelerometerSensor
    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensorList.size() > 0) {
            accelerometerPresent = true;
            accelerometerSensor = sensorList.get(0);
        } else {
            accelerometerPresent = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometerPresent) {
            sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (accelerometerPresent) {
            sensorManager.unregisterListener(accelerometerListener);
        }
    }


    // Sensor Listener Current condition is "FACE UP or FACE DOWN"
    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {

        }

        @Override
        public void onSensorChanged(SensorEvent arg0) {
            // TODO Auto-generated method stub
            float z_value = arg0.values[2];
            if (z_value >= 0) {
                // Current condition is "FACE UP" \\
                // do your stuff according to this\\
                resumeStream();
            } else {
                // Current condition is "FACE DOWN" \\
                // do your stuff according to this\\
                pauseStream();
            }
        }
    };

    // Pause Music
    public void pauseStream() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    // Resume Music
    public void resumeStream() {
        if (!player.isPlaying()) {
            player.start();
        }
    }
}