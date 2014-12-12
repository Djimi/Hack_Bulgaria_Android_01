package com.example.damyan.filedownloader;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Damyan on 26.11.2014 г..
 */
public class MyIntentService extends IntentService {


    public static final String OUT_DOWNLOADED = "OUT_DOWNLOADED";
    public static final String OUT_SIZE = "OUT_SIZE";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    public MyIntentService(){
        super("SOME");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Service", "start");;

        if(!intent.hasExtra(MyActivity.LINK)) return;
        String link = intent.getStringExtra(MyActivity.LINK);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MyActivity.MY_ACTION);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(MyActivity.LINK, link);


        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            File file = new File(Environment.getExternalStorageDirectory(), "some.mp4");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            InputStream inputStream = urlConnection.getInputStream();

            int size = urlConnection.getContentLength();

            int downloadedSize = 0;

            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //now, read through the input buffer and write the contents to the file
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutputStream.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
                //this is where you would do something to report the prgress, like this maybe



                broadcastIntent.putExtra(OUT_DOWNLOADED, downloadedSize);
                broadcastIntent.putExtra(OUT_SIZE, size);
                sendBroadcast(broadcastIntent);

            }
            //close the output stream when done
            fileOutputStream.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Service", "end");;

    }
}
