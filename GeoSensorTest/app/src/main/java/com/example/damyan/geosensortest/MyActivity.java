package com.example.damyan.geosensortest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.example.damyan.geosensortest.R;

import java.io.File;


public class MyActivity extends Activity {

    File file;
    VideoView view;
    boolean isPlaying;
    final String failName = "Ronaldo_Dive_Moti.mp4";

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        isPlaying = true;



//        File file = new File(Environment.getExternalStorageDirectory(), "Ronaldo_Dive_Moti.mp4");
        view = (VideoView)findViewById(R.id.video);
        view.setVideoPath(Environment.getExternalStorageDirectory() + "/" + failName);

//        view.setVideoURI(Uri.parse("https://github.com/HackBulgaria/Android-1/blob/master/week0/1-Widgets/Ronaldo_Dive_Moti.mp4"));
//        view.setVideoPath("https://github.com/HackBulgaria/Android-1/blob/master/week0/1-Widgets/Ronaldo_Dive_Moti.mp4");

        //setup previousButton
        ImageButton previousButton = (ImageButton)findViewById(R.id.previous);
        setupPreviousButton(previousButton);

        //setup playAndPauseButton
        ImageButton playAndPauseButton = (ImageButton)findViewById(R.id.playAndPause);
        setupPlayAndPauseButton(playAndPauseButton);


        //setup nextButton
        ImageButton nextButton = (ImageButton)findViewById(R.id.next);
        setupNextButton(nextButton);

        view.start();
    }

    public void setupPreviousButton(ImageButton previousButton){
        previousButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int currentTime = view.getCurrentPosition();
                view.seekTo(currentTime - 3000);
            }
        });
    }

    public void setupPlayAndPauseButton(ImageButton playAndPauseButton){
        playAndPauseButton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {

                ImageButton button = (ImageButton)findViewById(R.id.playAndPause);

                if(isPlaying == false){
                    button.setImageResource(R.drawable.pause);
                    view.start();
                } else {
                    button.setImageResource(R.drawable.play);
                    view.pause();
                }

                isPlaying = !isPlaying;
            }
        });
    }

    public void setupNextButton(ImageButton nextButton){
        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int currentTime = view.getCurrentPosition();
                view.seekTo(currentTime + 3000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
