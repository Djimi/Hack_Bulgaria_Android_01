package com.example.damyan.swipeandzoom;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyActivity extends Activity {

    TypedArray images;
    TypedArray names;
    ImageView currentImage;
    boolean isImageZoomed = true;
    int nextImageIndex = 0;
    TextView text;

    ProgressBar progressBar;

    RelativeLayout layout;

    GestureDetector gd;
    final int numberOfImages = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        layout = (RelativeLayout)findViewById(R.id.layout);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        currentImage = (ImageView)findViewById(R.id.picture);
        text = (TextView)findViewById(R.id.text);

        setDrawables();
        setNames();
        setNextImage();
        setProgressBar();
        setText();



        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d("Click", "ProgressBar: " + progressBar.getWidth() );
                Log.d("Click", "Current image: " + currentImage);

                isImageZoomed = !isImageZoomed;
                setImageSize();
                setProgressBar();
                setText();

                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if(Math.abs(e2.getX() - e1.getX()) < 200 || Math.abs(e2.getY() - e1.getY()) > 50) return true;


                //else
                nextImageIndex = (nextImageIndex + (e2.getX() > e1.getX() ? -1 : 1))%4;
                if(nextImageIndex < 0) nextImageIndex = numberOfImages - 1;

                Log.d("Click", "Nex image index: " + nextImageIndex);
                setNextImage();
                setProgressBar();
                setText();

                return true;
            }
        });




        currentImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gd.onTouchEvent(event);
                return true;
            }
        });


    }

    void setText(){
        text.setText((nextImageIndex + 1) + "/" + numberOfImages + "\n" + names.getString(nextImageIndex)) ;
    }

    void setProgressBar(){
        Log.d("Click", "Progress Bar: " + progressBar.getProgress());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(currentImage.getLayoutParams().width, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, R.id.picture);
        layout.updateViewLayout(progressBar, params);
        progressBar.setProgress(nextImageIndex+1);
    }

    void setNames(){
        names = getResources().obtainTypedArray(R.array.names);
    }

    void setImageSize(){
        if(isImageZoomed){
            RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            parameters.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.updateViewLayout(currentImage, parameters);

        } else {
            //get screen width
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;

            RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
            parameters.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.updateViewLayout(currentImage, parameters);
        }

    }

    public void setDrawables(){
        images = getResources().obtainTypedArray(R.array.images);
        nextImageIndex = 0;
    }

    public void setNextImage(){
        currentImage.setImageDrawable(images.getDrawable(nextImageIndex));

        setImageSize();

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
