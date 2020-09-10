package com.gasbuddy.imageloading;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private IParserListener iParserListener;
    private int requestCode;

    public HttpGetRequest(int requestCode, IParserListener iParserListener) {
        this.iParserListener = iParserListener;
        this.requestCode = requestCode;
    }

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = params[0];
        String result;
        String inputLine;
        try {
            URL myUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Authorization", "Client-ID 1p9ERLclx-R8eXzCaJmjHrm7nVr0ITqHqa-wrpLoox8");
            connection.connect();
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
            iParserListener.errorResponse(requestCode, e.getLocalizedMessage());
        }
        return result;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (result != null)
                if (requestCode == WSUtils.REQ_FOR_GET_PHOTO_LIST) {
                    JSONArray array = new JSONArray(result);
                    iParserListener.successResponse(requestCode, array);
                } else if (requestCode == WSUtils.REQ_FOR_GET_PHOTO_LIST_GOOGLE) {
                    JSONObject array = new JSONObject(result);
                    iParserListener.successResponse(requestCode, array);
                } else if (requestCode == WSUtils.REQ_FOR_GET_PHOTO_ITEM_DETAILS) {
                    JSONObject array = new JSONObject(result);
                    iParserListener.successResponse(requestCode, array);
                } else if (requestCode == WSUtils.REQ_FOR_GET_PHOTO_ITEM_DETAILS_GOOGLE) {
                    JSONObject array = new JSONObject(result);
                    iParserListener.successResponse(requestCode, array);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}