package com.example.damyan.puzzle_game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Damyan on 16.10.2014 г..
 */

public class MyDragShadowBuilder extends View.DragShadowBuilder {

    // The drag shadow image, defined as a drawable thing
    private static Drawable shadow;

    MyDragShadowBuilder(View v){
        super(v);

        shadow = new ColorDrawable(Color.BLUE);

    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
        int width, height;

        width = getView().getWidth()/2;
        height = getView().getHeight()/2;

        shadow.setBounds(0, 0, width, height);

        shadowSize.set(width, height);

        shadowTouchPoint.set(width/2, height/2);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        shadow.draw(canvas);
    }
}
