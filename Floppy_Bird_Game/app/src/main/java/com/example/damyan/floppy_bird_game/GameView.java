package com.example.damyan.floppy_bird_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * Created by Damyan on 29.10.2014 г..
 */
public class GameView extends ImageView implements GameClock.GameClockListener{

    private GameActionFragment gameActionFragment;
    private Bird bird;
    private ArrayList<ObstaclePair> obstaclePairs = new ArrayList<ObstaclePair>();
    private BackgroundLayers gameBackground;
    private BackgroundLayers ground;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap scoreBitmap;
    private Context context;
    Paint scorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        setupScorePaint();
    }

    public void increaseSpeedPerMove(float coefficient){
        GameOptions gameOptions = GameOptions.getInstance();

        int newBackgroundXSpeed = (int)(gameOptions.getBackgroundXSpeed()*coefficient);
        gameOptions.setBackgroundXSpeed(newBackgroundXSpeed);

        int newGroundAndObstaclesXSpeed = (int)(gameOptions.getGroundAndObstaclesXSpeed()*coefficient);
        gameOptions.setGroundAndObstaclesXSpeed(newGroundAndObstaclesXSpeed);


    }


    public GameView(Context context) {
        super(context);
        this.context = context;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        gameBackground.draw(canvas);
        bird.draw(canvas);

        for (ObstaclePair obstaclePair : obstaclePairs) {
            obstaclePair.draw(canvas);
        }

        ground.draw(canvas);

        GameState gs = GameState.getInstance();
        canvas.drawText("" + gs.getScore(), 400, 200, scorePaint);
    }

    private void setupScorePaint() {
        scorePaint.setColor(Color.GREEN);
        scorePaint.setAlpha(50);
        scorePaint.setTextSize(300);
    }

    public void addObstaclePair(ObstaclePair obstaclePair){
        obstaclePairs.add(obstaclePair);
    }

    public BackgroundLayers getGameBackground() {
        return gameBackground;
    }

    public void setGameBackground(BackgroundLayers gameBackground) {
        this.gameBackground = gameBackground;
    }

    public Bird getBird() {
        return bird;
    }

    public void setBird(Bird bird) {
        this.bird = bird;
    }

    private void sentGameEventToObjects(){
        gameBackground.onGameEvent();
        bird.onGameEvent();

        for(ObstaclePair obstaclePair : obstaclePairs) {
            obstaclePair.onGameEvent();
        }

        ground.onGameEvent();
    }

    public void checkForDeadlyCollision(){

        // collision looks bad, because of the rotation of the bird bitmap oO
        CollisionListener collisionListener = null;
        try{
             collisionListener = (CollisionListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement CollisionListener");

        }

        Rect birdRectangle = bird.getRectangle();

        if(birdRectangle.intersect(ground.getRectangle()) || bird.getY() <= 0){
            collisionListener.onCollisionDetect();
            return;
        }


        for (ObstaclePair obstaclePair : obstaclePairs) {

            Rect topObstacleRectangle = obstaclePair.getTopObstacle().getRectangle();
            Rect bottomObstacleRectangle = obstaclePair.getBottomObstacle().getRectangle();

            if (birdRectangle.intersect(topObstacleRectangle) || birdRectangle.intersect(bottomObstacleRectangle)) {
                collisionListener.onCollisionDetect();
                return ;
            }
        }


    }

    @Override
    public void onGameEvent() {
        Log.d("GameEvent", "Event");

        sentGameEventToObjects();
        gameActionFragment.getGameAction().updateState();
        invalidate();

    }

    public boolean isObstaclePassed() {
        for(ObstaclePair obstaclePair : obstaclePairs){

            if(!obstaclePair.isCounted() && bird.getX() + bird.getWidth() >= obstaclePair.getX() + obstaclePair.getWidth()/2){

                obstaclePair.setCounted(true);
                return true;
            }
        }

        return false;
    }

    public ArrayList<ObstaclePair> getObstaclePairs() {
        return obstaclePairs;
    }

    public void setGameActionFragment(GameActionFragment gameActionFragment) {
        this.gameActionFragment = gameActionFragment;
    }

    public void setGround(BackgroundLayers ground) {
        this.ground = ground;
    }

    public void startBirdFlying(){
        bird.startFlying();
    }

    public void reset() {

        obstaclePairs.clear();

        bird.setX(GameOptions.BIRD_START_X);
        bird.setY(GameOptions.BIRD_START_Y);


    }

    public interface CollisionListener{
        public void onCollisionDetect();
    }
}
