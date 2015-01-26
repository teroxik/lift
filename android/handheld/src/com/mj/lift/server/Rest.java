package com.mj.lift.server;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {
   private static String DEBUG_TAG = "REST";

   public static RestResponse executeRequest(LiftServer.LiftRequest request) throws IOException,JSONException {

       switch(request.getMethod()){
           case GET: {
               return get(request);
           }
           case POST: {
               return post(request);
           }
           case PUT: {
               return put(request);
           }
           case DELETE: {
               return null;
           }
           default:{
               return null;
           }
       }
   }


   private static RestResponse get(LiftServer.LiftRequest request) throws IOException,JSONException {

       InputStream is = null;
       HttpURLConnection con = null;
       try {
           URL url = new URL(request.getPath());
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

    private static RestResponse post(LiftServer.LiftRequest request) throws IOException,JSONException {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(request.getPath());
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setChunkedStreamingMode(0);

            os = con.getOutputStream();
            os.flush();
            if(request.getPayload() != null) {
                writePayload(request.getPayload(), os);
            }else if(request.getJsonPayload()!=null){
                con.setRequestProperty("Content-Type", "application/json; charset=utf8");
                writePayload(request.getJsonPayload().toString().getBytes("UTF-8"), os);
            }
            if(con.getResponseCode() == 200) {
                is = con.getInputStream();
            }
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


    private static RestResponse put(LiftServer.LiftRequest request) throws IOException,JSONException {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(request.getPath());
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setDoOutput(true);
            con.setRequestMethod("PUT");
            os = con.getOutputStream();
            os.flush();
            if(request.getPayload() != null) {
                writePayload(request.getPayload(), os);
            }else if(request.getJsonPayload()!=null){
                con.setRequestProperty("Content-Type", "application/json; charset=utf8");
                writePayload(request.getJsonPayload().toString().getBytes("UTF-8"), os);
            }
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
