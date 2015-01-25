package com.mj.lift;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.mj.lift.rest.Rest;
import com.mj.lift.rest.RestResponse;


public class Dashboard extends Activity implements SensorEventListener {

    public static final String PREFS_NAME = "LiftPrefs";
    public static final String APP_KEY = "APP_KEY";
    public static final String BACKEND_URL = "http://192.168.0.7:12552";
    private static String DEBUG_TAG = "Dashboard";


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    long lastUpdate = 0l;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserExists();
        setContentView(R.layout.main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result=data.getStringExtra("result");
                Log.d(DEBUG_TAG,result);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(APP_KEY, result);
                editor.commit();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    public void doLogin() {
        Intent i = new Intent(this, Login.class);
        startActivityForResult(i, 1);
    }


    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    protected void onResume() {
        super.onResume();
        checkUserExists();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void checkUserExists() {
        Log.i(DEBUG_TAG,"Check user exists.");

        String uuid = getSharedPreferences(PREFS_NAME,0).getString(APP_KEY,null);

        if(uuid == null) {
            Log.i(DEBUG_TAG,"Uuid = null -> login");
            doLogin();
        } else {
            Log.i(DEBUG_TAG,"Check if uuid is o?");
            new CheckCall().execute(BACKEND_URL + "/user/" + uuid + "/check");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                lastUpdate = curTime;
                TextView text = (TextView)findViewById(R.id.textView2);
                text.setText(""+x);
                text = (TextView)findViewById(R.id.textView4);
                text.setText(""+y);
                text = (TextView)findViewById(R.id.textView6);
                text.setText(""+z);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class CheckCall extends AsyncTask<String,String,RestResponse> {

        @Override
        protected RestResponse doInBackground(String... params){
            try {
                Log.i(DEBUG_TAG,"Check called");
                return Rest.GET(params[0]);

            } catch (Exception e ) {

                Log.d(DEBUG_TAG,e.getMessage());
                return new RestResponse(0,null);

            }
        }

        protected void onPostExecute(RestResponse restResponse){
           if(200 != restResponse.getCode()) {
               Log.i(DEBUG_TAG,"User id doesn't exist do the login.");
               doLogin();
           }
        }
    }


}
