package com.example.damyan.floppy_bird_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Damyan on 29.10.2014 г..
 */
public class BackgroundLayers extends GameObject{

    Rect leftSrcRect;
    Rect rightSrcRect;
    Rect leftDstRect;
    Rect rightDstRect;

    public BackgroundLayers(Bitmap bitmap, int x, int y) {
        super(bitmap, x, y);

        leftSrcRect = new Rect();
        rightSrcRect = new Rect();
        leftDstRect = new Rect();
        rightDstRect = new Rect();
    }

    @Override
    protected void draw(Canvas canvas) {

        if(getX() < -Math.abs(getBitmap().getWidth())){
            setX(0);
        }


        canvas.drawBitmap(getBitmap(), getX(), getY(), getPaint());
        canvas.drawBitmap(getBitmap(), getX() + getBitmap().getWidth(), getY(), getPaint());

    }

    @Override
    public void onGameEvent() {
        nextMove(GameOptions.getInstance().getBackgroundXSpeed(), 0);
    }
}
