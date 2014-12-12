package com.example.damyan.splashscreen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MyActivity extends Activity {

    public static String HAS_BEEN_STARTED = "HAS_BEEN_STARTED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        if(!sharedPreferences.contains(HAS_BEEN_STARTED)){
            sharedPreferences.edit().putBoolean(HAS_BEEN_STARTED, true).commit();

            Handler handler = new Handler();
            final ImageView hackView = ((ImageView) findViewById(R.id.hackBulgariaView));
            final TextView mainText = ((TextView)findViewById(R.id.helloText));


            hackView.setVisibility(View.VISIBLE);
            mainText.setVisibility(View.GONE);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hackView.setVisibility(View.GONE);
                    mainText.setVisibility(View.VISIBLE);
                }
            }, 3000);

        };

    }


}
