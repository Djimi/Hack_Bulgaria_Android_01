package com.example.damyan.detectsomeoneisnear;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;


public class HardPushActivity extends Activity implements SensorEventListener{

    public static final int SPEED_THRESHOLDER = 300;
    public static final int TIME_THRESHOLDER = 100;
    private SensorManager sm;
    private Sensor accelerometer;
    private MediaPlayer mediaPlayer;

    private float oldX = 0;
    private float oldY = 0;
    private float oldZ = 0;

    long lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gun_layout);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mediaPlayer = MediaPlayer.create(this, R.raw.ray_gun);
        lastTime = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.seekTo(0);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sm.unregisterListener(this);
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long diffTime = System.currentTimeMillis() - lastTime;
        if(diffTime > TIME_THRESHOLDER) {
            if ((x + y + z + oldX - oldY - oldZ) / diffTime * 1000 > SPEED_THRESHOLDER && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
            lastTime = System.currentTimeMillis();
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
