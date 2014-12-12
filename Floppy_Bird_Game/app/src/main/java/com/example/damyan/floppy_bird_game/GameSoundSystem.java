package com.example.damyan.floppy_bird_game;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by Damyan on 10.11.2014 г..
 */
public class GameSoundSystem {

    private MediaPlayer backgroundMediaPlayer;
    private SoundPool soundPool;

    private int jumpMusicID;
    private int deadChickenID;
    private int nextLevelID;

    private static boolean isSet;

    private static GameSoundSystem gameSoundSystem = new GameSoundSystem();

    private GameSoundSystem() {
    }

    public static GameSoundSystem getInstance() {
        return isSet ? gameSoundSystem : null;
    }

    public static void createAndSetupSoundSystem(Context context) {
        isSet = true;
        gameSoundSystem.createPlayers(context);
        gameSoundSystem.configurePlayers(context);

    }

    private void configurePlayers(Context context) {
        jumpMusicID = soundPool.load(context, R.raw.jump_sound_like_mario, 1);
        deadChickenID = soundPool.load(context, R.raw.chicken_dinner, 1);
        nextLevelID = soundPool.load(context, R.raw.next_level, 1);

        backgroundMediaPlayer.setLooping(true);
    }

    private void createPlayers(Context context) {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        backgroundMediaPlayer = MediaPlayer.create(context, R.raw.main_music);
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public MediaPlayer getBackgroundMediaPlayer() {
        return backgroundMediaPlayer;
    }

    public void playDeadlyChickenSound(){
        soundPool.play(deadChickenID, 100, 100, 1, 0, 1f);
    }

    public void playJumpSound(){
        soundPool.play(jumpMusicID, 100, 100, 1, 0, 1f);
    }

    public void playNextLevelSound(){
        soundPool.play(nextLevelID, 100, 100, 1, 0, 1f);
    }

    public void playBackgroundSound(){
        backgroundMediaPlayer.start();
    }

    public void startBackgroundMusic(){
        backgroundMediaPlayer.start();
    }


    public void pauseSounds() {
        backgroundMediaPlayer.pause();
        soundPool.autoPause();
    }

    public void stopBackgroundMusic() {
        backgroundMediaPlayer.stop();
    }
}