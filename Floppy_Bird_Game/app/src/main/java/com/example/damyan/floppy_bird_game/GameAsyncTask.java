package com.example.damyan.floppy_bird_game;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

/**
 * Created by Damyan on 25.11.2014 г..
 */
public class GameAsyncTask extends AsyncTask<Void, String, String> {

    WeakReference<Fragment> fragment;

    public GameAsyncTask(WeakReference<Fragment> fragment) {
        this.fragment = fragment;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("Begin", "Yes");

            String responseStr  = null;


            HttpClient httpClient = new DefaultHttpClient();
            HttpPut putRequest = new HttpPut("http://95.111.103.224:8080/Flappy/scores");
            putRequest.addHeader("Content-Type", " application/json");
            JSONObject loginObject = new JSONObject();
            try {

                //everywhere checks whether fragment != null (because it`s weak reference)
                int buttonId = ((RadioGroup)fragment.get().getView().findViewById(R.id.schoolMenu)).getCheckedRadioButtonId();

                String school = null;
                if(buttonId == R.id.SURadioButton){
                    school = "SU";
                } else if(buttonId == R.id.TURadioButton){
                    school = "TU";
                } else if(buttonId == R.id.TUESRadioButton){
                    school = "TUES";
                }

                loginObject.put("name", ((EditText)fragment.get().getView().findViewById(R.id.usernameField)).getText().toString());
                loginObject.put("mail", ((EditText)fragment.get().getView().findViewById(R.id.emailField)).getText().toString());
                loginObject.put("whereFrom", school);
                loginObject.put("score", GameState.getInstance().getScore());
                putRequest.setEntity(new StringEntity(loginObject.toString()));
                HttpResponse response = httpClient.execute(putRequest);
                StatusLine statusLine = response.getStatusLine();

                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    responseStr = "Connection to server established";
                    Log.d("Http", "Pushka");
                } else {
                    responseStr = "Fail";
                    Log.d("Http", "" + statusLine.getStatusCode());
                    response.getEntity().getContent().close();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseStr;
        }

        @Override
        protected void onPostExecute(String serverResponse) {
            try {
                ((OnServerResponseListener) fragment.get()).onServerResponse(serverResponse);
            } catch (ClassCastException e){
                throw new ClassCastException(fragment.get().getClass().getName() + " should implement " + OnServerResponseListener.class);
            }

            TextView textView = (TextView)fragment.get().getActivity().findViewById(R.id.serverResponse);
            textView.setTextColor(Color.BLUE);
            textView.setText(serverResponse);
        }
}
