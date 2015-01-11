package com.mj.lift;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    public static final String BACKEND_URL = "";

    //user put - login
    //user post - creation
    //user/userId/check

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

    public void doLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
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
        String uuid = getSharedPreferences(PREFS_NAME,0).getString(APP_KEY,null);

        if(uuid == null) {
            doLogin();
        } else {
            new CheckCall().execute("BACKEND_URL" + "/user/" + uuid + "/check");
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

                return Rest.GET(params[0]);

            } catch (Exception e ) {

                System.out.println(e.getMessage());

                return new RestResponse(0,"");

            }
        }

        protected void onPostExecute(RestResponse restResponse){
           if(200 != restResponse.getCode()) {
               doLogin();
           }
        }
    }


}
