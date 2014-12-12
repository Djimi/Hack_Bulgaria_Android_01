package com.example.damyan.floppy_bird_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import java.util.Random;

/**
 * Created by Damyan on 1.11.2014 г..
 */
public class GameAction {


    private Context context;
    private GameView gameView;
    private Point screenDimensionWithoutGround;


    public GameAction(Context context, GameView gameView, Point screenDimension) {
        this.context = context;
        this.gameView = gameView;
        this.screenDimensionWithoutGround = new Point(screenDimension.x, screenDimension.y - GameOptions.GROUND_HEIGHT);

        addNewObstaclePair();
    }


    private boolean shouldAddObstacle() {

        int lastIndex = gameView.getObstaclePairs().size() - 1;

        if(lastIndex < 0 ) return true;

        float distancePassedByLastObstacle = screenDimensionWithoutGround.x - gameView.getObstaclePairs().get(lastIndex).getX();
        if(GameOptions.OBSTACLES_MIN_DISTANCE < distancePassedByLastObstacle) {
            return true;
        }

        return false;
    }


    public void updateState() {



        GameSoundSystem gameSoundSystem = GameSoundSystem.getInstance();
        GameState gameState = GameState.getInstance();


        if (gameView.isObstaclePassed()) {
            gameState.increaseScore();

            if (shouldChangeTheLevel()) {
                increaseLevel();
                gameSoundSystem.playNextLevelSound();
            }
        }


        if (shouldAddObstacle()) {
            addNewObstaclePair();
        }

        if (firstObstacleIsOutOfTheScreen()) {
            ObstaclePair obstaclePair = gameView.getObstaclePairs().get(0);
            obstaclePair.getBottomObstacle().getBitmap().recycle();
            obstaclePair.getTopObstacle().getBitmap().recycle();

            gameView.getObstaclePairs().remove(0);
        }
        gameView.checkForDeadlyCollision();

    }

    private boolean firstObstacleIsOutOfTheScreen() {
        return !gameView.getObstaclePairs().get(0).isInScreen();
    }

    private void addNewObstaclePair() {

        Random random = new Random();

        int uStartY = 0;
        int uEndY = uStartY + random.nextInt((int)(screenDimensionWithoutGround.y*GameOptions.OBSTACLES_CREATION_COEFFICIENT ));
        int dStartY = (int)(uEndY + screenDimensionWithoutGround.y*(1 - GameOptions.OBSTACLE_SCREEN_Y_RATIO));
        int dEndY = screenDimensionWithoutGround.y;


        Bitmap obstaclePairBitmap = null;

        if(!ObstaclePair.isBitmapSetup()){
            obstaclePairBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.obstacle);
        }

        int additionalDistance = random.nextInt(GameOptions.OBSTACLES_MAX_DISTANCE - GameOptions.OBSTACLES_MIN_DISTANCE);
        int x = additionalDistance + screenDimensionWithoutGround.x;

        // here we pass uEndY + 1, because otherwise there is a chance uEndY to be 0 and an
        //exception will occur in obstacle constructor
        ObstaclePair obstaclePair = new ObstaclePair(obstaclePairBitmap, x, uStartY, uEndY + 1, dStartY, dEndY + 1);


        gameView.addObstaclePair(obstaclePair);

    }

    private void increaseLevel() {

        GameState.getInstance().increaseLevel();

        if(GameState.getInstance().getLevel() < 7) gameView.increaseSpeedPerMove(GameOptions.LEVEL_SPEED_COEFFICIENT);
    }

    private boolean shouldChangeTheLevel() {
        GameState gameState = GameState.getInstance();

        return gameState.getScore()% GameOptions.OBSTACLES_PER_LEVEL == 0 ? true : false;
    }



}
