package com.mj.lift;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mj.lift.server.LiftServer;
import com.mj.lift.server.Rest;
import com.mj.lift.server.RestResponse;
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
                String uuid = getSharedPreferences(Dashboard.PREFS_NAME,0).getString(Dashboard.APP_KEY,null);
                //To be replaced by some clever way
                LiftServer server = new LiftServer();
                LiftServer.LiftRequest req = null;
                if(uuid == null)
                    req = server.createRequest(LiftServer.Api.USER_REGISTER);
                else
                    req = server.createRequest(LiftServer.Api.USER_LOGIN);
                req.setJsonPayload(user);
                return Rest.executeRequest(req);

            } catch (Exception e ) {

                Log.d(DEBUG_TAG, "Exception "+ e.getMessage(),e);

                return new RestResponse(0,null);

            }
        }

        protected void onPostExecute(RestResponse restResponse){
            if(restResponse.getCode() == 200) {
                try {
                    Intent i = new Intent();
                    String id = restResponse.getResponseBody().getString("id");
                    i.putExtra("id",id);
                    Log.d(DEBUG_TAG,"Id je: "+id);
                    setResult(RESULT_OK, i);
                    new RegisterDeviceCall().execute(id);
                    finish();
                } catch(JSONException je) {
                    Log.d(DEBUG_TAG,je.getMessage());
                }
            } else {
                Toast.makeText(this.getApplication,"Returned code: "+restResponse.getCode(),Toast.LENGTH_SHORT);
            }
        }
    }

    private class RegisterDeviceCall extends AsyncTask<String,String,RestResponse> {
        @Override
        protected RestResponse doInBackground(String... params){
            try {
                //To be replace by some service
                LiftServer server = new LiftServer(params[0]);
                LiftServer.LiftRequest req = server.createRequest(LiftServer.Api.USER_REGISTER_DEVICE);
                return Rest.executeRequest(req);

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
