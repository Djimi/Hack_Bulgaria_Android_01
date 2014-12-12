package com.example.damyan.filedownloader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


public class MyActivity extends Activity {

    public static final String LINK = "LINK";
    public static final String DIABLO_NAME = "DIABLO";
    public static final String OBICHAM_NAME = "OBICHAM";
    public static final String PARKING_NAME = "PARKING";
    public static final String PENNY_NAME = "PENNY";

    public static final String MY_ACTION = "com.example.damyan";

    Button[] buttons;
    String[] links;
    ProgressBar[] progressBars;

    BroadcastReceiver receiver;
    IntentFilter filter;
    Intent intentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        setupLinks();
        setupButtons();

        setupProgressBars();

        receiver = new MyBroadcastReceiver();

        filter = new IntentFilter(MY_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        intentService = new Intent(MyActivity.this, MyIntentService.class);

    }

    private void setupProgressBars() {
        progressBars = new ProgressBar[4];
        progressBars[0] = ((ProgressBar)findViewById(R.id.diabloProgressBar));
        progressBars[1] = ((ProgressBar)findViewById(R.id.obichamProgressBar));
        progressBars[2] = ((ProgressBar)findViewById(R.id.parkingGeniusProgressBar));
        progressBars[3] = ((ProgressBar)findViewById(R.id.pennyProgressBar));
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, filter);
    }


    @Override
    protected void onPause() {
        unregisterReceiver(receiver);

        super.onPause();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {

            String link = intent.getStringExtra(LINK);


            for(int i = 0; i < 4; ++i){
                if(link.equals(links[i])) {
                    int progress = intent.getIntExtra(MyIntentService.OUT_DOWNLOADED, -1);
                    int size = intent.getIntExtra(MyIntentService.OUT_SIZE, -1);

                    progressBars[i].setMax(size);
                    Log.d("Progress", "progres: "+ progress);
                    progressBars[i].setProgress(progress);

                    if(progress == size){
                        Notification notify =
                                new Notification.Builder(MyActivity.this)
                                        .setSmallIcon(R.drawable.ice_cream)
                                        .setContentTitle("File Downloaded")
                                        .setContentText("The file from link " + link + " has been downloaded !" )
                                        .build();

                        NotificationManagerCompat.from(MyActivity.this).notify(0, notify);
                    }
                }
            }
        }
    }

    private void setupLinks() {
        links = new String[4];
        links[0] = "http://95.111.103.224:8080/static/diablo_vs_imperius.mp4";
        links[1] = "http://95.111.103.224:8080/static/obicham.mp3";
        links[2] = "http://95.111.103.224:8080/static/parking_genius.mp4";
        links[3] = "http://95.111.103.224:8080/static/penny.mp4";
    }

    private void setupButtons() {
        buttons = new Button[4];
        buttons[0] = (Button)findViewById(R.id.diabloButton);
        buttons[1] = (Button)findViewById(R.id.obichamButton);
        buttons[2] = (Button)findViewById(R.id.parkingGeniusButton);
        buttons[3] = (Button)findViewById(R.id.pennyButton);

        for(int i = 0; i < 4; ++i){
            final int temp = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   intentService.putExtra(LINK, links[temp]);
                    startService(intentService);
                }
            });
        }
    }


}
