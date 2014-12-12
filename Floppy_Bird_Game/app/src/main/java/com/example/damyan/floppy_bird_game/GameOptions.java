package com.example.damyan.floppy_bird_game;

/**
 * Created by Damyan on 10.11.2014 г..
 */
public class GameOptions {

    public static final int INITIAL_BIRD_Y_SPEED = 10;
    public static final int INITIAL_BACKGROUND_X_SPEED = -5;
    public static final int INITIAL_GROUND_AND_OBSTACLES_X_SPEED = -5;
    public final static int OBSTACLES_PER_LEVEL = 5;
    public final static float LEVEL_SPEED_COEFFICIENT = 1.2f;
    public final static int OBSTACLES_MIN_DISTANCE = 500;
    public final static int OBSTACLES_MAX_DISTANCE = 800;
    public final static int GROUND_HEIGHT = 100;
    public final static int BIRD_START_X = 100;
    public final static int BIRD_START_Y = 300;
    public final static float OBSTACLE_SCREEN_Y_RATIO = 1/2f;


    private int birdYSpeed;
    private int backgroundXSpeed;
    private int groundAndObstaclesXSpeed;

    private static final GameOptions instance = new GameOptions();
    public static final float OBSTACLES_CREATION_COEFFICIENT = 2f/5f;

    private GameOptions() {
        birdYSpeed = INITIAL_BIRD_Y_SPEED;
        backgroundXSpeed = INITIAL_BACKGROUND_X_SPEED;
        groundAndObstaclesXSpeed= INITIAL_GROUND_AND_OBSTACLES_X_SPEED;
    }

    public static GameOptions getInstance(){
        return instance;
    }

    public int getBirdYSpeed() {
        return birdYSpeed;
    }

    public void setBirdYSpeed(int birdYSpeed) {
        this.birdYSpeed = birdYSpeed;
    }

    public int getBackgroundXSpeed() {
        return backgroundXSpeed;
    }

    public void setBackgroundXSpeed(int backgroundXSpeed) {
        this.backgroundXSpeed = backgroundXSpeed;
    }

    public int getGroundAndObstaclesXSpeed() {
        return groundAndObstaclesXSpeed;
    }

    public void setGroundAndObstaclesXSpeed(int groundAndObstaclesXSpeed) {
        this.groundAndObstaclesXSpeed = groundAndObstaclesXSpeed;
    }


}
