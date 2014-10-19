package com.example.damyan.puzzle_game;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MyActivity extends Activity {

    /** Called when the activity is first created. */

    private int piecesInRow = 4;
    private int piecesInCow = 4;
    private final int piecesCount = piecesInCow*piecesInRow; // this is not necessary


    ArrayList<Drawable> drawablePiecesInRightOrder;
    ArrayList<ImageView> imagePieces;

    GridLayout grid;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        getActionBar().hide();


        grid = (GridLayout)findViewById(R.id.grid);
//        grid = new GridLayout(this);
        grid.setOrientation(GridLayout.HORIZONTAL); // just in case
        grid.setRowCount(piecesInRow);
        grid.setColumnCount(piecesInCow);

        imagePieces = new ArrayList<ImageView>(piecesCount);

        setDrawablePiecesInRightOrder();
        ArrayList<Drawable> shuffledPieces = new ArrayList<Drawable>(drawablePiecesInRightOrder){{
            Collections.shuffle(this);
        }};


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        for(int i = 0; i < piecesCount; ++i){
            final ImageView image = new ImageView(this);
            image.setImageDrawable(shuffledPieces.get(i));
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            GridLayout.LayoutParams l = new GridLayout.LayoutParams();
            l.height = height/piecesInCow - 2;
            l.width = width/piecesInRow - 2;
            image.setLayoutParams(l);
            image.setPadding(2, 2, 2, 2);
            imagePieces.add(image);

            image.setOnDragListener(new OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    int action = event.getAction();

                    ImageView host = (ImageView)v;
                    ImageView guest = (ImageView)event.getLocalState();
                    if(host == guest) return true;
                    Log.d("Drag", "Action");
                    switch (action) {
                        case DragEvent.ACTION_DROP:

                                Log.d("Drag", "Action: " + "Host == guest");
                                Drawable temp = host.getDrawable();
                                host.setImageDrawable(guest.getDrawable());
                                guest.setImageDrawable(temp);

                        case DragEvent.ACTION_DRAG_ENDED :

                            Log.d("Drag", "Action: Ended");

                            if(win(imagePieces)){
                                Toast.makeText(MyActivity.this, "Winner, Winner - chicken dinner ", Toast.LENGTH_LONG).show();
                            }
                    }

                    return true;
                }

            });


            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.startDrag(null , new View.DragShadowBuilder(v), v, 0);
                    return true;
                }
            });

            grid.addView(image);
        }




    }

    private boolean win(ArrayList<ImageView> other){

        ArrayList<Drawable> temp = new ArrayList<Drawable>(piecesCount);

        for(ImageView view : other){
            temp.add(view.getDrawable());
        }

        return drawablePiecesInRightOrder.equals(temp);
    }

    private void setDrawablePiecesInRightOrder(){
        TypedArray temp = getResources().obtainTypedArray(R.array.pictures);
        drawablePiecesInRightOrder = new ArrayList<Drawable>(piecesCount);

        for(int i = 0; i < temp.length(); ++i){
            drawablePiecesInRightOrder.add(temp.getDrawable(i));
        }

        temp.recycle();

    }
}