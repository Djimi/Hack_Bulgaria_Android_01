package com.example.damyan.floppy_bird_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Damyan on 29.10.2014 г..
 */
public class ObstaclePair implements GameClock.GameClockListener{

    boolean counted = false;

    Obstacle topObstacle;
    Obstacle bottomObstacle;

    private static Bitmap pairBitmap;

    public ObstaclePair(Bitmap bitmap, int x, int uStartY, int uEndY, int dStartY, int dEndY) {

        if(!isBitmapSetup()) pairBitmap = bitmap;

        Bitmap topObstacleBitmap = Bitmap.createScaledBitmap(pairBitmap, pairBitmap.getWidth(), uEndY - uStartY, false);
        topObstacle = new Obstacle(topObstacleBitmap, x, uStartY);

        Bitmap bottomObstacleBitmap = Bitmap.createScaledBitmap(pairBitmap, pairBitmap.getWidth(), dEndY - dStartY, false);
        bottomObstacle = new Obstacle(bottomObstacleBitmap, x, dStartY);
    }

    protected void draw(Canvas canvas) {
        topObstacle.draw(canvas);
        bottomObstacle.draw(canvas);
    }

    @Override
    public void onGameEvent() {
        nextMove();
    }

    private void nextMove() {
        GameOptions gameOptions = GameOptions.getInstance();

        topObstacle.nextMove(gameOptions.getGroundAndObstaclesXSpeed(), 0);
        bottomObstacle.nextMove(gameOptions.getGroundAndObstaclesXSpeed(), 0);
    }

    public boolean isCounted() {
        return counted;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }

    public boolean isInScreen() {
        return topObstacle.getX() + topObstacle.getWidth() >= 0;
    }

    public int getX(){
        return topObstacle.getX();
    }

    public Obstacle getTopObstacle() {
        return topObstacle;
    }

    public Obstacle getBottomObstacle() {
        return bottomObstacle;
    }

    public int getWidth() {
        return topObstacle.getWidth();
    }

    public static boolean isBitmapSetup(){
        return pairBitmap != null;
    }
}
