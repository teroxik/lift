package com.mj.lift;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.mj.lift.rest.Rest;
import com.mj.lift.rest.RestResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends Activity implements View.OnClickListener {

    private static String DEBUG_TAG = "Login";

    private EditText login;
    private EditText password;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button button = (Button)findViewById(R.id.login);
        button.setOnClickListener(this);

        login = (EditText) findViewById(R.id.userEmail);
        password = (EditText) findViewById(R.id.userPass);

    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        new LoginCall().execute(login.getText().toString(),password.getText().toString());
    }


    private class LoginCall extends AsyncTask<String,String,RestResponse> {

        @Override
        protected RestResponse doInBackground(String... params){
            try {

                Log.i(DEBUG_TAG,"Log in call");
                String url = Dashboard.BACKEND_URL + "/user";
                JSONObject user = new JSONObject();
                user.put("email", params[0]);
                user.put("password", params[1]);
                return Rest.POST(url, user);

            } catch (Exception e ) {

                Log.d(DEBUG_TAG, "Exception " + e.getMessage());

                return new RestResponse(0,null);

            }
        }

        protected void onPostExecute(RestResponse restResponse){
            if(restResponse.getCode() == 200) {
                try {
                    Intent i = new Intent();
                    String id = restResponse.getResponseBody().getString("id");
                    i.putExtra("id",id);
                    setResult(RESULT_OK,i);
                    new RegisterDeviceCall().execute(id);
                    finish();
                } catch(JSONException je) {
                    Log.d(DEBUG_TAG,je.getMessage());
                }
            }
        }
    }

    private class RegisterDeviceCall extends AsyncTask<String,String,RestResponse> {
        @Override
        protected RestResponse doInBackground(String... params){
            try {
                String url = Dashboard.BACKEND_URL + "/user" +params[0]+ "/device/android";
                return Rest.POST(url, null);

            } catch (Exception e ) {

                Log.d(DEBUG_TAG, "Exception " + e.getMessage());

                return new RestResponse(0,null);

            }
        }

        protected void onPostExecute(RestResponse restResponse){
            if(restResponse.getCode() == 200) {
                try {
                    Intent i = new Intent();
                    i.putExtra("id",restResponse.getResponseBody().getString("id"));
                    setResult(RESULT_OK,i);
                    finish();
                } catch(JSONException je) {
                    Log.d(DEBUG_TAG,je.getMessage());
                }
            }
        }
    }


}
