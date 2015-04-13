package com.example.vedikajadhav.assignment3.dummy;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vedika Jadhav on 3/20/2015.
 */
public class HttpInstructorDetailTask extends AsyncTask<String, Void, ArrayList<String>>{
    private static final String TAG = "HttpInstructorDetailTask";
    String responseBody;
    ArrayList<String> combinedResponse = new ArrayList<String>();
    OnTaskFinishedListener mOnTaskFinishedListener;
    AndroidHttpClient mAndroidHttpClient;

    public HttpInstructorDetailTask(AndroidHttpClient instructorDetailHttpClient, Context context) {
        mAndroidHttpClient = instructorDetailHttpClient;
        mOnTaskFinishedListener = (OnTaskFinishedListener)context;
    }

    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        try {
            for (int i = 0; i < urls.length; i++) {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpGet getMethod = new HttpGet(urls[i]);
                responseBody = mAndroidHttpClient.execute(getMethod, responseHandler);
                combinedResponse.add(responseBody);
            }
            return combinedResponse;
        } catch (Throwable throwable) {
            Log.i(TAG, "did not work", throwable);
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(ArrayList<String> result) {
        if (mOnTaskFinishedListener != null) {
            mOnTaskFinishedListener.onDetailedFinished(result);
        }
    }

    public void setOnTaskFinishedListener(OnTaskFinishedListener mListener){
        mOnTaskFinishedListener = mListener;
    }
}

