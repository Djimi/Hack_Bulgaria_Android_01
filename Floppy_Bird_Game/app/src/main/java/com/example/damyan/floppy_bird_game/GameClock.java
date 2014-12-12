package com.example.damyan.floppy_bird_game;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
* Created by Damyan on 29.10.2014 г..
*/

public class GameClock {
    public static final int FRAMERATE_CONSTANT = 16;

    private List<GameClockListener> clockListeners = new ArrayList<GameClockListener>();
    private boolean isPlaying = false;

    public static interface GameClockListener {
        public void onGameEvent();
    }

    private Handler handler = new Handler();

    public GameClock() {
        handler.post(new ClockRunnable());
    }

    public void subscribe(GameClockListener listener) {
        clockListeners.add(listener);
    }

    private class ClockRunnable implements Runnable {
        @Override
        public void run() {
            if(isPlaying) {
                onTimerTick();
                handler.postDelayed(this, FRAMERATE_CONSTANT);
            }
        }

        private void onTimerTick() {
            for (GameClockListener listener : clockListeners) {
                 listener.onGameEvent();
            }
        }


    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}