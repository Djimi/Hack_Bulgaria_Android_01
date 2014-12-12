package com.example.damyan.gesture_image;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import java.util.ArrayList;


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

    Button playButton;
    Button checkPointButton;
    Button resetAnimationButton;

    float startPictureX;
    float startPictureY;


    AnimatorSet animatorSet;
    int durationOfAnimation = 500;
    ArrayList<Animator> animators;

    float[] lastState;

    private void init(){
        screen = findViewById(R.id.screen);
        picture = findViewById(R.id.picture);
        animatorSet = new AnimatorSet();
        lastState = new float[5];
        animators = new ArrayList<Animator>();

        startPictureX = picture.getX();
        startPictureY = picture.getY();
        Log.d("Coordinates", "Init X: " + startPictureX);

        Log.d("Coordinates", "Init Y: " + startPictureY);

        lastState[0] = picture.getX();
        lastState[1] = picture.getY();
        lastState[2] = picture.getRotation();
        lastState[3] = picture.getScaleX();
        lastState[4] = picture.getScaleY();

        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        getActionBar().hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        init();


        screen.setOnTouchListener(new MyTouchListener());

        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d("Click", "double tap");
                picture.setX(startPictureX);
                picture.setY(startPictureY);
                picture.setTranslationY(0);
                picture.setTranslationX(0);
                picture.setRotation(0);
                picture.setScaleX(1);
                picture.setScaleY(1);

                return true;
            }
        });

        picture.setOnTouchListener(new View.OnTouchListener() {

           @Override
            public boolean onTouch(View v, MotionEvent event) {

//                gd.onTouchEvent(event);

               if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                   isInImage = true;

               }

               return false;
           }

        });

        playButton = (Button)findViewById(R.id.playButton);
        resetAnimationButton = (Button)findViewById(R.id.resetAnimationButton);
        checkPointButton = (Button)findViewById(R.id.makeCheckPointButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < animators.size(); ++i){
                    Log.d("Animators", "Animator " + i + " " + animators.get(i));
                }
                animatorSet.playSequentially(animators);
                animatorSet.setDuration(durationOfAnimation);
                animatorSet.start();
                animatorSet = new AnimatorSet();
            }
        });

        resetAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSet = new AnimatorSet();
                animators.clear();
            }
        });

        checkPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("x", lastState[0], picture.getX());
                PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("y", lastState[1], picture.getY());

                PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", lastState[2], picture.getRotation());

                PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", lastState[3], picture.getScaleX());
                PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", lastState[4], picture.getScaleY());

                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(picture, translationX, translationY, rotation, scaleX, scaleY);

                animators.add(animator);


                lastState[0] = picture.getX();
                lastState[1] = picture.getY();
                lastState[2] = picture.getRotation();
                lastState[3] = picture.getScaleX();
                lastState[4] = picture.getScaleY();

                Log.d("Coordinates", "CheckPoint X: " + lastState[0]);
                Log.d("Coordinates", "CheckPoint Y: " + lastState[1]);
            }
        });

    }

    class MyTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(!isInImage) return true;

             gd.onTouchEvent(event);

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

                   initializeFirstCoordinates(event);
                   initializeSecondCoordinates(event);
                   setOldPointBetweenFingersCoordinates((firstX + secondX)/2, (firstY + secondY)/2);

                   pivotLineAngle = angleBetweenLineAndXAxis(event); // new


                } else if(event.getActionMasked() == MotionEvent.ACTION_MOVE) {

                    float angle = (float)angleBetweenPivotLineAndLine(event); //new

                     picture.setRotation(picture.getRotation() + angle); //new



                    float coefficient = calculateCoefficient(event);
                    setPictureScale(coefficient);
                    Log.d("Picture", "Scale: " + picture.getScaleX() + " " + picture.getScaleY());

                    initializeFirstCoordinates(event);
                    initializeSecondCoordinates(event);

                    setNewPointBetweenFingersCoordinates((firstX + secondX)/2, (firstY + secondY)/2);
                    setPictureTranslationWithPointBetweenFingers();
                    setOldPointBetweenFingersCoordinates(newPointBetweenFingersX, newPointBetweenFingersY);

                    pivotLineAngle = (pivotLineAngle + angle)%360;


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
