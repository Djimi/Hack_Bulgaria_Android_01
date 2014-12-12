package com.example.damyan.floppy_bird_game;

/**
 * Created by Damyan on 10.11.2014 г..
 */
public class GameState {

    private int score;
    private int level;

    private static final GameState instance = new GameState();

    private GameState(){
        score = 0;
        level = 1;
    }

    public static GameState getInstance(){
        return instance;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(){
        ++score;
    }

    public int getLevel() {
        return level;
    }

    public void increaseLevel(){
        ++level;
    }

    public void resetState(){
        score = 0;
        level = 1;
    }
}
