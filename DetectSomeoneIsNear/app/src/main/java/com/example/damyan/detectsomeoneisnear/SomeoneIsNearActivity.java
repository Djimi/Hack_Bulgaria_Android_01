package com.example.damyan.detectsomeoneisnear;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;


public class SomeoneIsNearActivity extends Activity implements SensorEventListener, MediaPlayer.OnPreparedListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private int hellMusidID;
    MediaPlayer mediaPlayer ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mediaPlayer = MediaPlayer.create(this, R.raw.hell);
        mediaPlayer.setOnPreparedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.seekTo(0);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0] == 0 && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
    }
}
