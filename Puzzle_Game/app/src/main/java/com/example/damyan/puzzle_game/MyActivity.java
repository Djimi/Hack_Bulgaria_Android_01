package com.example.damyan.puzzle_game;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
        grid.setOrientation(GridLayout.HORIZONTAL); // just in case
        grid.setRowCount(piecesInRow);
        grid.setColumnCount(piecesInCow);

        setImagePiecesInShuffledOrder();


    }


    private boolean win(){

        for(int i = 0; i < piecesCount; ++i){
//            if( (Integer)imagePieces.get(i).getTag() >= (Integer)imagePieces.get(i + 1).getTag() ){
//                Log.d("Indexes", "On " + i + " iteration");
//                Log.d("Indexes", "current - next : " + (Integer)imagePieces.get(i).getTag() + " - " + (Integer)imagePieces.get(i + 1).getTag());
//
//                return false;
//            }

            if((Integer)(imagePieces.get(i).getTag()) != i){
                Log.d("Indexes", "On "  + i + " possition is " + imagePieces.get(i).getTag());

                return false;
            }
            Log.d("Indexes", "On "  + i + " possition is " + imagePieces.get(i).getTag());

        }


        return true;
    }

    private void setImagePiecesInShuffledOrder(){

        imagePieces = new ArrayList<ImageView>(piecesCount);

        // take numbers from 0 to ( piecesCount - 1 )
        ArrayList<Integer> numbersInShuffledOrder = new ArrayList<Integer>(piecesCount);
        for(int i = 0; i < piecesCount; ++i){
            numbersInShuffledOrder.add(i);
        }
        Collections.shuffle(numbersInShuffledOrder);

        TypedArray temp = getResources().obtainTypedArray(R.array.pictures);
//
        // get screen dimention
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        for(int i = 0; i < piecesCount; ++i){
            Integer index = numbersInShuffledOrder.get(i);

            final ImageView view = new ImageView(this);
            view.setImageDrawable(temp.getDrawable(index));
            view.setTag(index);

            view.setAdjustViewBounds(true);
            view.setScaleType(ImageView.ScaleType.FIT_XY);



            GridLayout.LayoutParams l = new GridLayout.LayoutParams();
            l.height = height/piecesInCow - 2;
            l.width = width/piecesInRow - 2;
            view.setLayoutParams(l);
            view.setPadding(2, 2, 2, 2);
            imagePieces.add(view);

            view.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    int action = event.getAction();

                    ImageView host = (ImageView) v;
                    ImageView guest = (ImageView) event.getLocalState();

                    Log.d("Drag", "Action");
                    switch (action) {
                        case DragEvent.ACTION_DROP:


                            Log.d("Drag", "Action: " + "Host == guest");


                            float hostX = host.getX();
                            float hostY = host.getY();

                            float guestX = guest.getX();
                            float guestY = guest.getY();


                            PropertyValuesHolder translateHostX = PropertyValuesHolder.ofFloat("x", hostX, guestX);
                            PropertyValuesHolder translateHostY = PropertyValuesHolder.ofFloat("y", hostY, guestY);

                            PropertyValuesHolder translateGuestX = PropertyValuesHolder.ofFloat("x", guestX, hostX);
                            PropertyValuesHolder translateGuestY = PropertyValuesHolder.ofFloat("y", guestY, hostY);


                            ObjectAnimator animatorHost = ObjectAnimator.ofPropertyValuesHolder(host, translateHostX, translateHostY);
                            ObjectAnimator animatorGuest = ObjectAnimator.ofPropertyValuesHolder(guest, translateGuestX, translateGuestY);

                            int duration = 250;
                            animatorHost.setDuration(duration);
                            animatorGuest.setDuration(duration);


                            animatorHost.start();
                            animatorGuest.start();

                            //swap possitions in the imagePieces array list

                            int hostIndex = imagePieces.indexOf(host);
                            int guestIndex = imagePieces.indexOf(guest);

                            ImageView temp = imagePieces.get(hostIndex);
                            imagePieces.set(hostIndex, imagePieces.get(guestIndex));
                            imagePieces.set(guestIndex, temp);

                            // swp tags of textview
//                            Object tempTag = host.getTag();
//                            host.setTag(guest.getTag());
//                            guest.setTag(tempTag);

                        case DragEvent.ACTION_DRAG_ENDED:

                            Log.d("Drag", "Action: Ended");
                            if (win()) {
                                Toast.makeText(MyActivity.this, "Winner, Winner - chicken dinner ", Toast.LENGTH_SHORT).show();
                            }
                    }

                    return true;
                }

            });

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.startDrag(null, new View.DragShadowBuilder(v), v, 0);
                    return true;
                }
            });

            grid.addView(view);
        }

        temp.recycle();
    }

}
