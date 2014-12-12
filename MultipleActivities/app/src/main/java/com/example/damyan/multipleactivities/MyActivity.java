package com.example.damyan.multipleactivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MyActivity extends Activity {

    public static final String NUMBER_REGULAR_EXPRESSION = "[0-9\\+]{3,20}";
    public static final String NUMBER = "number";
    public static final String WEBPAGE_REGULAR_EXPRESSION = "[a-z]+\\.[a-z]+(\\.[a-z]+)";
    public static final String WEBPAGE = "webpage";
    public static final String ALARM = "alarm";
    public static final String ALARM_REGULAR_EXPRESSION = "[0-9][0-9]:[0-9][0-9]";
    public static final String TOAST_MESSAGE = "This action does not match to ";
    EditText editText;
    Button dialButon;
    Button browseButton;
    Button alarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        setupButtons();
        setupEditText();

    }

    private void setupEditText() {
        editText = (EditText)findViewById(R.id.editTextField);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String result = textMatch(editText.getText().toString());

                if(result != null){
                    editText.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.smalltick), null);
                } else {
                    editText.setError("This is not correct");
                }

            }
        });
    }

    private String textMatch(String str) {
        if(str.matches(NUMBER_REGULAR_EXPRESSION)){
            return NUMBER;
        }

        if(str.matches(WEBPAGE_REGULAR_EXPRESSION)){
            return WEBPAGE;
        }

        if(str.matches(ALARM_REGULAR_EXPRESSION)){
            return ALARM;
        }

        return "";
    }


    private void setupButtons() {
        dialButon = (Button)findViewById(R.id.dialButton);
        browseButton = (Button)findViewById(R.id.browseButton);
        alarmButton = (Button)findViewById(R.id.alarmButton);
    }

    public void dial(View view){

        if(textMatch(editText.getText().toString()).equals(NUMBER)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + editText.getText().toString()));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            makeToastForErrorAction(NUMBER);
        }
    }

    public void browse(View view){


        if(textMatch(editText.getText().toString()).equals(WEBPAGE)){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + editText.getText().toString()));

            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        } else {
            makeToastForErrorAction(WEBPAGE);
        }
    }

    public void setNewAlarm(View view){
        if(textMatch(editText.getText().toString()).equals(ALARM)){
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                    .putExtra(AlarmClock.EXTRA_MESSAGE, "Ставай мърда такава !")
                    .putExtra(AlarmClock.EXTRA_HOUR, 14)
                    .putExtra(AlarmClock.EXTRA_MINUTES, 56);


            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        } else {
            makeToastForErrorAction(ALARM);
        }
    }

    private void makeToastForErrorAction(String text) {
        Toast.makeText(this, TOAST_MESSAGE + text, Toast.LENGTH_SHORT).show();
    }


}
