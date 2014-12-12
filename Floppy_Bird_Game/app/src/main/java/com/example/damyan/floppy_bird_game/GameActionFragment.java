package com.example.damyan.floppy_bird_game;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class GameActionFragment extends Fragment implements GameView.CollisionListener {

    private GameClock gameClock;
    private GameView gameView;
    private GameAction gameAction;

    private Point screenDimension;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createGameView();

        // get screen dimension
        screenDimension = getScreenDimension();

        gameView.setGameActionFragment(this);
        gameAction = new GameAction(getActivity().getApplicationContext(), gameView, getScreenDimension());

        init();

        return gameView;
    }

    private Point getScreenDimension(){
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);

        return point;
    }

    private void createGameView() {
        //making gameView
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        gameView = new GameView(getActivity());
        gameView.setLayoutParams(layoutParams);
    }

    @Override
    public void onResume() {
        super.onResume();


        GameSoundSystem.getInstance().startBackgroundMusic();

        gameClock = new GameClock();
        gameClock.subscribe(gameView);
        gameClock.setPlaying(true);

    }

    @Override
    public void onPause() {
        Log.d("Game Pause", "Pause");
        super.onPause();
        gameClock.setPlaying(false);

        GameSoundSystem.getInstance().stopBackgroundMusic();
    }

    private void pauseMediaPlayers() {
        GameSoundSystem gameSoundSystem = GameSoundSystem.getInstance();
        gameSoundSystem.pauseSounds();
        gameClock.setPlaying(false);
    }

    private void init() {

        GameSoundSystem.createAndSetupSoundSystem(getActivity());
        setupBirdBitmap();
        setupBackground();
        setupGround();
        setupScreen();
    }

    private void setupGround() {
        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        Bitmap backgroundBitmap = Bitmap.createScaledBitmap(temp, screenDimension.x, 100, false);
        final BackgroundLayers ground = new BackgroundLayers(backgroundBitmap, 0, screenDimension.y - GameOptions.GROUND_HEIGHT);
        gameView.setGround(ground);
    }

    private void setupBackground() {


        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.clouds);
        Bitmap backgroundBitmap = Bitmap.createScaledBitmap(temp, screenDimension.x, screenDimension.y - GameOptions.GROUND_HEIGHT, false);
        final BackgroundLayers background = new BackgroundLayers(backgroundBitmap, 0, 0);
        gameView.setGameBackground(background);
    }

    private void setupBirdBitmap() {
        Bitmap birdBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird);
        final Bird bird = new Bird(birdBitmap, GameOptions.BIRD_START_X, GameOptions.BIRD_START_Y);
        gameView.setBird(bird);
    }


    private void setupScreen() {

        gameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               gameView.startBirdFlying();
               GameSoundSystem.getInstance().playJumpSound();
            }
        });
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }



    public GameAction getGameAction() {
        return gameAction;
    }

    @Override
    public void onCollisionDetect() {
        gameClock.setPlaying(false);
    }
}
