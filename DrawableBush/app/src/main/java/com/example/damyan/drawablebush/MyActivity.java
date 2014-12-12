package com.example.damyan.drawablebush;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MyActivity extends Activity {

    LinearLayout screen;
    Drawable currentDrawable;
    MyImageView view;

    Bitmap angryBirdBitmap;
    Bitmap eyeBitmap;
    Bitmap unihornBitmap;

    GestureDetector gd;


    boolean justCleared = true;
    View currentSelectedBrush = null;

    boolean isStarted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


//        angryBirdBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.angry_bird);
//        eyeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eye);
//        unihornBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.animate_unihorn);

        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){


            @Override
            public void onLongPress(MotionEvent e) {

                if(currentSelectedBrush != null){
                    currentSelectedBrush.setBackgroundResource(R.drawable.red_gradient_background);
                    currentSelectedBrush = null;
                }

                Point point = new Point();
                getWindowManager().getDefaultDisplay().getSize(point);

                Bitmap bitmap = Bitmap.createBitmap(point.x, point.y, Bitmap.Config.ARGB_8888);
                view.setCurrentBitmap(null);
                view.invalidate();
                view.setCanvasBitmap(bitmap);

                justCleared = true;
            }
        });

        screen = (LinearLayout)findViewById(R.id.screen);
        view = (MyImageView)findViewById(R.id.myView);

        int width = view.getLayoutParams().width;
        int height = view.getLayoutParams().height;

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        Bitmap bitmap = Bitmap.createBitmap(point.x, point.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        view.setCanvas(canvas);
        view.setCanvasBitmap(bitmap);



        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(!isStarted) {
                    if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(getApplicationContext(), "Click 'Start' and choose a brush !!!", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }

                if(justCleared){
                    if(event.getActionMasked() == MotionEvent.ACTION_DOWN && isStarted){
                        Toast.makeText(getApplicationContext(), "Choose a brush !!!", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }

                gd.onTouchEvent(event);

                view.setCurrentX(event.getX() - currentSelectedBrush.getWidth()/2);
                view.setCurrentY(event.getY() - currentSelectedBrush.getHeight()/2);
                view.invalidate();

                return true;
            }
        });
    }

    public void onClick(View v){

        if(!isStarted){
            Toast.makeText(this, "You have to press 'Start' to load the bitmaps !!!", Toast.LENGTH_SHORT).show();
            return;
        }

        justCleared = false;

        if(v == currentSelectedBrush) return;

        // else
        switch (v.getId()){
            case R.id.eye :
                view.setCurrentBitmap(eyeBitmap);
                break;
            case R.id.angryBird:
                view.setCurrentBitmap(angryBirdBitmap);
                break;
            case R.id.animateUnihorn:
                view.setCurrentBitmap(unihornBitmap);
                break;
        }



        if(currentSelectedBrush != null){
            currentSelectedBrush.setBackgroundResource(R.drawable.red_gradient_background);
        }


        v.setBackgroundResource(R.drawable.green_gradient_background);
        currentSelectedBrush = v;
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
        if (id == R.id.start) {
            isStarted = true;

            View v;
            Bitmap temp;


            v = findViewById(R.id.angryBird);
            temp = BitmapFactory.decodeResource(getResources(), R.drawable.angry_bird);
            angryBirdBitmap = Bitmap.createScaledBitmap(temp, v.getWidth(), v.getHeight(), false);


            v = findViewById(R.id.eye);
            temp = BitmapFactory.decodeResource(getResources(), R.drawable.eye);
            eyeBitmap = Bitmap.createScaledBitmap(temp, v.getWidth(), v.getHeight(), false);


            v = findViewById(R.id.animateUnihorn);
            temp = BitmapFactory.decodeResource(getResources(), R.drawable.animate_unihorn);
            unihornBitmap = Bitmap.createScaledBitmap(temp, v.getWidth(), v.getHeight(), false);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
