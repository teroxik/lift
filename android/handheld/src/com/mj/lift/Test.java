package com.mj.lift;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.mj.lift.rest.Rest;
import com.mj.lift.rest.RestResponse;
import org.json.JSONException;

public class Test extends Activity implements View.OnClickListener {


    private static String DEBUG_TAG = "Test";
    private static final String SESSION_KEY = "SESSION";

    private String sessionId;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        sessionId = "";

        Button button = (Button)findViewById(R.id.startButton);
        button.setOnClickListener(this);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    private void toast(String txt) {
        Toast toast = Toast.makeText(this, txt, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onClick(View v) {

        Log.d(DEBUG_TAG,"Click v "+v.toString());

        String sessionId = getSharedPreferences(Dashboard.PREFS_NAME,0).getString(SESSION_KEY,null);
        String userId = getSharedPreferences(Dashboard.PREFS_NAME,0).getString(Dashboard.APP_KEY,null);

        if(v.getId() == R.id.startButton){
            new StartSessionCall().execute(userId);
            toast("Start session executed");
        }else if(v.getId() == R.id.testButton){
            new SendData().execute(userId,sessionId);
            toast("Send data executed");
        } else if(v.getId() == R.id.stopButton){
            new StopSessionCall().execute(userId,sessionId);
            toast("End session executed");
        }
    }

    private class StartSessionCall extends AsyncTask<String,String,RestResponse> {

        @Override
        protected RestResponse doInBackground(String... params){
            try {
                String url = Dashboard.BACKEND_URL + "/exercise/"+params[0]+"/start";
                return Rest.POST(url, null);
            } catch (Exception e ) {

                Log.d(DEBUG_TAG, "Exception " + e.getMessage());

                return new RestResponse(0,null);

            }
        }

        protected void onPostExecute(RestResponse restResponse){
            if(restResponse.getCode() == 200) {
                try {
                    restResponse.getResponseBody().getString("sessionId");
                    SharedPreferences settings = getSharedPreferences(Dashboard.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(SESSION_KEY, sessionId);
                    editor.commit();
                } catch(JSONException je) {
                    Log.d(DEBUG_TAG,je.getMessage());
                }
            }
        }
    }


    private class SendData extends AsyncTask<String,String,RestResponse> {

        @Override
        protected RestResponse doInBackground(String... params){
            try {
                String url = Dashboard.BACKEND_URL+ "exercise/"+params[0] +"/"+params[1];
                return Rest.PUT(url,"test".getBytes());
            } catch (Exception e ) {
                Log.d(DEBUG_TAG, "Exception " + e.getMessage());
                return new RestResponse(0,null);
            }
        }

        protected void onPostExecute(RestResponse restResponse){
            if(restResponse.getCode() == 200) {
                Log.i(DEBUG_TAG,"Data sucessfully sent");
            } else {
                Log.e(DEBUG_TAG,restResponse.toString());
            }
        }
    }

    private class StopSessionCall extends AsyncTask<String,String,RestResponse> {

        @Override
        protected RestResponse doInBackground(String... params){
            try {

                String url = Dashboard.BACKEND_URL+ "exercise/"+params[0] +"/"+params[1]+"/stop";
                return Rest.POST(url, null);
            } catch (Exception e ) {
                Log.d(DEBUG_TAG, "Exception " + e.getMessage());
                return new RestResponse(0,null);

            }
        }

        protected void onPostExecute(RestResponse restResponse){
            if(restResponse.getCode() == 200) {
                Log.i(DEBUG_TAG,"Exercise end finished sucessfully");
            } else {
                Log.e(DEBUG_TAG,restResponse.toString());
            }
        }
    }

}
