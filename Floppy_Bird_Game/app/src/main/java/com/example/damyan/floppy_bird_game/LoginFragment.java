package com.example.damyan.floppy_bird_game;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Damyan on 12.11.2014 г..
 */
public class LoginFragment extends Fragment implements OnServerResponseListener {

    private Button loginButton;
    private GameStartListener gameStartListener;

    View view;
    private ProgressBar progressBar;
    private TextView serverResponseField;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.login_fragment_layout, null);

        init();
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setupGameStartListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = ((ProgressBar)getActivity().findViewById(R.id.progressBar));
        serverResponseField = ((TextView)getActivity().findViewById(R.id.serverResponse));
    }

    private void init() {
        setupLoginButton();
        setupPlayButton();
    }

    private void setupPlayButton() {
        ((Button)view.findViewById(R.id.play_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStartListener.onGameStart();
            }
        });
    }

    private void setupLoginButton() {

        loginButton = (Button)view.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentScoreToServer();
            }
        });
    }


    private void setupGameStartListener() {
        try {
            gameStartListener = (GameStartListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement " + GameStartListener.class);
        }
    }


    public interface GameStartListener{
        public void onGameStart();
    }

    private void sentScoreToServer() {
        progressBar.setVisibility(View.VISIBLE);

        GameAsyncTask task = new GameAsyncTask(new WeakReference<Fragment>(this));
        task.execute();
    }

    @Override
    public void onServerResponse(String serverResponse) {

        serverResponseField.setText(serverResponse);
        progressBar.setVisibility(View.INVISIBLE);
        serverResponseField.setVisibility(View.VISIBLE);
    }
}

