package com.example.damyan.floppy_bird_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Damyan on 29.10.2014 г..
 */
public class Bird extends GameObject{

    private int jumpHeight = 100;
    private int remainingToRaise = 100;

    private boolean isDead = false;

    private static Bitmap[] bitmaps;


    public Bird(Bitmap bitmap, int x, int y) {
        super(bitmap, x, y);

        createBitmaps();
    }

    private void createBitmaps() {
        int minAngle = -50;
        int maxAngle = 50;
        int differenceInAnglesBetweenBitmaps = 10;


        bitmaps = new Bitmap[(maxAngle - minAngle ) / differenceInAnglesBetweenBitmaps + 1 ];
        int counter = 0;

        for(int angle = -50 ; angle <= 50; angle += 10){
            bitmaps[counter] = GameActionFragment.rotateBitmap(getBitmap(), angle);
            ++counter;
        }
    }

    @Override
    protected void draw(Canvas canvas) {

        int sign = remainingToRaise > 0 ? -1 : 1;
        float rotationAngle = sign * (Math.abs(remainingToRaise) < 50 ? Math.abs(remainingToRaise) : 50);

        Bitmap tempBitmap = bitmaps[5 + (int)rotationAngle/10];
        canvas.drawBitmap(tempBitmap, getX(), getY(), getPaint());
       }

    public void startFlying() {
        if(remainingToRaise < 0 ) remainingToRaise = 0;

        remainingToRaise += jumpHeight;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    @Override
    public void onGameEvent() {
        GameOptions gameOptions = GameOptions.getInstance();

        if(remainingToRaise > 0){
            gameOptions.setBirdYSpeed(-Math.abs(gameOptions.getBirdYSpeed()));
            remainingToRaise += gameOptions.getBirdYSpeed();
        } else {
            gameOptions.setBirdYSpeed(Math.abs(gameOptions.getBirdYSpeed()));
            remainingToRaise -= gameOptions.getBirdYSpeed();
        }

        nextMove(0, gameOptions.getBirdYSpeed());
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }
}
