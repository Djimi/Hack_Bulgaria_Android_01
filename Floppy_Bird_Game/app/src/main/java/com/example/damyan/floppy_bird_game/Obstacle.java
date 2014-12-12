package com.example.damyan.floppy_bird_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Damyan on 3.11.2014 г..
 */
public class Obstacle extends GameObject{

    public Obstacle(Bitmap bitmap, int x, int y) {
        super(bitmap, x, y);
    }

    @Override
    protected void draw(Canvas canvas) {
        canvas.drawBitmap(getBitmap(), getX(), getY(), getPaint());
    }

    @Override
    public void onGameEvent() {
        nextMove(GameOptions.getInstance().getGroundAndObstaclesXSpeed(), 0);
    }
}
