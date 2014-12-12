package com.example.damyan.floppy_bird_game;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by Damyan on 12.11.2014 г..
 */
public class GameActivity extends Activity implements LoginFragment.GameStartListener, GameView.CollisionListener{

    public static final String LOGIN_FRAGMENT = "loginFragment";
    public static final String GAME_ACTION_FRAGMENT = "gameActionFragment";

    GameActionFragment gameActionFragment;
    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAppWindow();
        setContentView(R.layout.activity_game_layout);

        layout = (FrameLayout)findViewById(R.id.root);

        gameActionFragment = new GameActionFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.root, gameActionFragment, GAME_ACTION_FRAGMENT);
        ft.commit();

    }

    private void setupAppWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // enable full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public void onCollisionDetect() {

        final GameSoundSystem gameSoundSystem = GameSoundSystem.getInstance();

        gameSoundSystem.stopBackgroundMusic();
        gameSoundSystem.playDeadlyChickenSound();
        GameState.getInstance().resetState();



        LoginFragment fragment = new LoginFragment();

        try {
            ((GameView.CollisionListener) getFragmentManager().findFragmentByTag(GAME_ACTION_FRAGMENT)).onCollisionDetect();
        }catch (ClassCastException e){
            throw new ClassCastException(GAME_ACTION_FRAGMENT + " should implement CollisionListener !");
        }


        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.game_view_comes, R.animator.login_out)
                .replace(R.id.root, fragment, LOGIN_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onGameStart() {

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.game_view_comes, R.animator.login_out)
                .replace(R.id.root, gameActionFragment, GAME_ACTION_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }
}
