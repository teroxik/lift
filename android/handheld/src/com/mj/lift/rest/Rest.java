package com.mj.lift.rest;

import android.util.Log;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {
   private static String DEBUG_TAG = "REST";

   public static RestResponse GET(String urlString) throws IOException {

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

           // Convert the InputStream into a string
           String contentAsString = readResponse(is);
           return new RestResponse(response,contentAsString);
       } finally {
           if (is != null) {
               is.close();
           }
           if(con != null) {
               con.disconnect();
           }
       }
   }

    public static RestResponse POST(String urlString, JSONObject payload) throws IOException {
        InputStream is = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setDoOutput(true);
            con.setChunkedStreamingMode(0);
            con.setRequestProperty("Content-Type", "application/json; charset=utf8");

            writePayload(payload.toString().getBytes("UTF-8"),con.getOutputStream());

            String contentAsString = readResponse(is);
            return new RestResponse(con.getResponseCode(), contentAsString);
        } finally {
            if (is != null) {
                is.close();
            }
            if(con != null) {
                con.disconnect();
            }
        }
    }


    public static void writePayload(byte[] payload, OutputStream stream) throws IOException, UnsupportedEncodingException {
        stream.write(payload);
    }

    public static String readResponse(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
             sb.append(line);
        }
        return sb.toString();
    }

}
