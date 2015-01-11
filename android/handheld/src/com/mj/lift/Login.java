package com.mj.lift;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.mj.lift.rest.Rest;
import com.mj.lift.rest.RestResponse;
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
        String url = Dashboard.BACKEND_URL + "/user";
        // String user = "{user: '"+login.getText()+"' , password: '"+password.getText()+"'}";
        JSONObject user = new JSONObject();
        try {
            user.put("email", login.getText());
            user.put("password", password.getText());
            RestResponse response = Rest.POST(url, user);

            SharedPreferences settings = getSharedPreferences(Dashboard.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Dashboard.APP_KEY, response.getResponseBody());
            editor.commit();

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "The response is: " + e.getMessage());
        }
    }
}
