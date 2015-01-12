package com.mj.lift.rest;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {
   private static String DEBUG_TAG = "REST";

   public static RestResponse GET(String urlString) throws IOException,JSONException {

       InputStream is = null;
       HttpURLConnection con = null;
       try {
           URL url = new URL(urlString);
           con = (HttpURLConnection) url.openConnection();
           con.setReadTimeout(10000 /* milliseconds */);
           con.setConnectTimeout(15000 /* milliseconds */);
           // Starts the query
           con.connect();
           int response = con.getResponseCode();
           Log.d(DEBUG_TAG, "The response is: " + response);
           is = con.getInputStream();

           return new RestResponse(response, readResponse(is));
       } finally {
           if (is != null) {
               is.close();
           }
           if(con != null) {
               con.disconnect();
           }
       }
   }

    public static RestResponse POST(String urlString, JSONObject payload) throws IOException,JSONException {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setChunkedStreamingMode(0);
            con.setRequestProperty("Content-Type", "application/json; charset=utf8");
            os = con.getOutputStream();
            os.flush();
            writePayload(payload.toString().getBytes("UTF-8"),os);
            is = con.getInputStream();
            return new RestResponse(con.getResponseCode(),  readResponse(is));
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if(con != null) {
                con.disconnect();
            }
        }
    }


    public static void writePayload(byte[] payload, OutputStream stream) throws IOException, UnsupportedEncodingException {
        stream.write(payload);
    }

    public static JSONObject readResponse(InputStream stream) throws IOException,JSONException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.length() > 0 ? new JSONObject(sb.toString()) : null;
    }
}
