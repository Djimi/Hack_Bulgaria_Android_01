package com.example.damyan.floppy_bird_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by Damyan on 29.10.2014 г..
 */
public abstract class GameObject implements GameClock.GameClockListener{

    private int x;
    private int y;

    private int width;
    private int height;

    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Rect rectangle;


    public GameObject(Bitmap bitmap, int x, int y){
        setBitmap(bitmap);
        setX(x);
        setY(y);

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        rectangle = new Rect(x, y, x + width, y + height);
    }

    public void nextMove(int moveToX, int moveToY){
        x += moveToX;
        y += moveToY;

        rectangle.set(x, y, x + width, y + height);

    }

    protected abstract void draw(Canvas canvas);

    Rect getRectangle(){
        return rectangle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    PointF getPosition(){
        return new PointF(x, y);
    }

    int getWidth(){
        return width;
    }

    int getHeight(){
        return height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getY() {

        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
