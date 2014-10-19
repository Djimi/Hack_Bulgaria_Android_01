package com.example.damyan.gesture_image;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


public class MyActivity extends Activity{

    private View screen;
    private View picture;

    private float firstX;
    private float firstY;

    private float secondX;
    private float secondY;

    private float newPointBetweenFingersX;
    private float newPointBetweenFingersY;

    private float oldPointBetweenFingersX;
    private float oldPointBetweenFingersY;

    boolean secondFingerJustUp = false;
    boolean isInImage = false;

    float lastTangent;
    // this angle is acording to X-axis
    double pivotLineAngle;

    GestureDetector gd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        screen = findViewById(R.id.screen);
        picture = findViewById(R.id.picture);



        getActionBar().hide();

        screen.setOnTouchListener(new MyTouchListener());

        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d("Click", "double tap");
                picture.setTranslationY(0);
                picture.setTranslationX(0);
                picture.setRotation(0);
                picture.setScaleX(1);
                picture.setScaleY(1);

                return false;
            }
        });

        picture.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Click", "In touch: " + isInImage);
//                gd.onTouchEvent(event);

                if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    isInImage = true;
                }

                return false;
            }

        });

    }

    class MyTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(!isInImage) return true;

            gd.onTouchEvent(event);


            Log.d("Click", "Root in touch");

            if(event.getPointerCount() == 1) {

                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

                    initializeFirstCoordinates(event);

                } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {

                    // if one of the fingers is upped, than just set firstX and firstY to the coordinates of the other finger
                    // this is necessary, because if firstX and firstY was set to the coordinate of the upped finger, than we will have motion of the picture,
                    // which is not desired
                    if(secondFingerJustUp){
                        secondFingerJustUp = false;
                    } else {
                         setPictureTranslationWithEventAndPoint(event, firstX, firstY);
                    }

                    initializeFirstCoordinates(event);
                } else if(event.getActionMasked() == MotionEvent.ACTION_UP){
                    isInImage = false;
                }
            } else if(event.getPointerCount() == 2) {

                if(event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){

//                    lastTangent = (event.getY(0) - event.getY(1))/(event.getX(0) - event.getX(1));

                   initializeFirstCoordinates(event);
                   initializeSecondCoordinates(event);
                   setOldPointBetweenFingersCoordinates((firstX + secondX)/2, (firstY + secondY)/2);

                   pivotLineAngle = angleBetweenLineAndXAxis(event); // new


                } else if(event.getActionMasked() == MotionEvent.ACTION_MOVE) {

                    float angle = (float)angleBetweenPivotLineAndLine(event); //new
//                    Log.d("Angle ", "PIvotAngle: " + pivotLineAngle + "\n " + angleBetweenLineAndXAxis(event.getX(0), event.getY(0), event.getX(1), event.getY(1)) +"\n" + angle );
                    picture.setRotation(picture.getRotation() + angle); //new
//                    Log.d("Angle", "Rotation to: " + picture.getRotation());



                    float coefficient = calculateCoefficient(event);
                    setPictureScale(coefficient);
                    Log.d("Picture", "Scale: " + picture.getScaleX() + " " + picture.getScaleY());

                    initializeFirstCoordinates(event);
                    initializeSecondCoordinates(event);

                    setNewPointBetweenFingersCoordinates((firstX + secondX)/2, (firstY + secondY)/2);
                    setPictureTranslationWithPointBetweenFingers();
                    setOldPointBetweenFingersCoordinates(newPointBetweenFingersX, newPointBetweenFingersY);

                    pivotLineAngle = (pivotLineAngle + angle)%360;

//                    Log.d("Click", "" + picture.getRotation());

                } else if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
                    secondFingerJustUp = true;
                }
            }



            return true;
        }

    }

    //takes coordinates of two points and calculate the angle between the pivot Line and
    //the line formed from this two points



    double angleBetweenPivotLineAndLine(MotionEvent event){
        double angle = angleBetweenLineAndXAxis(event);

        return angle - pivotLineAngle;
    }

    double angleBetweenLineAndXAxis(MotionEvent event){


        if(event.getX(0) == event.getX(1)) {
            if(event.getY(0) < event.getY(1)) return 90;
            else return 270;
        }

        int additionalAngle = 0;


        float angleTangent = (event.getY(0) - event.getY(1))/(event.getX(0) - event.getX(1));

        if(event.getX(1) < event.getX(0)) additionalAngle = 180;


//        Log.d("Angle", "Angle tangent: " + angleTangent + "\n" + (some + (180/Math.PI)*Math.atan(angleTangent)));
        return additionalAngle + (180/Math.PI)*Math.atan(angleTangent);
    }

    void setOldPointBetweenFingersCoordinates(float x, float y){
        oldPointBetweenFingersX = x;
        oldPointBetweenFingersY = y;
    }
    void setNewPointBetweenFingersCoordinates(float x, float y){
        newPointBetweenFingersX = x;
        newPointBetweenFingersY = y;
    }

    void setPictureTranslationWithEventAndPoint(MotionEvent event, float x, float y){
        picture.setTranslationX(picture.getTranslationX() + event.getX() - x);
        picture.setTranslationY(picture.getTranslationY() + event.getY() - y);
    }
    void setPictureTranslationWithPointBetweenFingers(){
        picture.setTranslationX(picture.getTranslationX() + newPointBetweenFingersX - oldPointBetweenFingersX);
        picture.setTranslationY(picture.getTranslationY() + newPointBetweenFingersY - oldPointBetweenFingersY);
    }

    void setPictureScale(float coefficient){
        picture.setScaleX(picture.getScaleX()*coefficient);
        picture.setScaleY(picture.getScaleY()*coefficient);
    }

    void initializeFirstCoordinates(MotionEvent event){
        firstX = event.getX();
        firstY = event.getY();
    }

    void initializeSecondCoordinates(MotionEvent event){
        secondX = event.getX(1);
        secondY = event.getY(1);
    }
    float calculateCoefficient(MotionEvent event){
        float oldDistance = distance_between_points(firstX, firstY, secondX, secondY);
        float newDistance = distance_between_points(event.getX(0), event.getY(0), event.getX(1), event.getY(1));

        return newDistance/oldDistance;
    }


    float distance_between_points(float x1, float y1, float x2, float y2) {
        double distancePow2 = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
        return (float)Math.sqrt(distancePow2);
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
