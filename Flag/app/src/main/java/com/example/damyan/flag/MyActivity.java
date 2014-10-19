package com.example.damyan.flag;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.Random;


public class MyActivity extends Activity {
    int[] rainbow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulgarian_flag);



        rainbow = this.getResources().getIntArray(R.array.rainbow);

        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(new ButtonListener());

        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new ButtonListener());

        Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new ButtonListener());
    }


    class ButtonListener implements OnClickListener{
        Random rand = new Random();

        @Override
        public void onClick(View v) {
            int current = rand.nextInt(rainbow.length);
            v.setBackgroundColor(rainbow[current]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
