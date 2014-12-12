package com.example.damyan.drawablebush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Damyan on 25.10.2014 г..
 */
public class MyImageView extends ImageView {

    Canvas canvas;
    Bitmap currentBitmap;
    Bitmap canvasBitmap;

    public Bitmap getCanvasBitmap() {
        return canvasBitmap;
    }

    public void setCanvasBitmap(Bitmap canvasBitmap) {
        canvas.setBitmap(canvasBitmap);
        this.canvasBitmap = canvasBitmap;
    }

    float currentX;
    float currentY;


    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context) {
        super(context);

    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas systemCanvas) {
        super.onDraw(systemCanvas);

        if(currentBitmap != null && canvasBitmap != null) {

            Paint paintWithAlpha = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintWithAlpha.setAlpha(125);
            Paint paintWithoutAlpha = new Paint(Paint.ANTI_ALIAS_FLAG);


            canvas.drawBitmap(currentBitmap, currentX, currentY, paintWithAlpha);
            systemCanvas.drawBitmap(canvasBitmap, 0, 0, paintWithoutAlpha);
        }
    }

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public float getCurrentX() {
        return currentX;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }



}
