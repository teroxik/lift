package com.mj.lift.rest;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.mj.lift.Dashboard;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michaljanousek on 25/01/2015.
 */
public class Get extends AsyncTask<String,String,RestResponse> {

    private static final String DEBUG_TAG = "Get_Task";

    @Override
    protected RestResponse doInBackground(String... params){
        try {
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

}
