package com.example.damyan.flashlight;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;


public class MyActivity extends Activity {


    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        camera = Camera.open();
        Camera.Parameters parameters= camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    protected void onDestroy() {
        camera.release();
        super.onDestroy();
    }
}
