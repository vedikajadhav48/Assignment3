package com.example.vedikajadhav.assignment3.dummy;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

/**
 * Created by Vedika Jadhav on 3/20/2015.
 */
public class HttpInstructorListTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "HttpInstructorListTask";
    String responseBody;
    OnTaskFinishedListener mOnTaskFinishedListener;
    AndroidHttpClient mAndroidHttpClient;

    public HttpInstructorListTask(AndroidHttpClient instructorListHttpClient,Context context ) {
        mAndroidHttpClient = instructorListHttpClient;
        mOnTaskFinishedListener = (OnTaskFinishedListener)context;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            for(int i = 0; i < urls.length; i++) {
                Log.i(TAG, "url" + urls[i]);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpGet getMethod = new HttpGet(urls[i]);
                responseBody = mAndroidHttpClient.execute(getMethod, responseHandler);
            }
            return responseBody;
        }catch (Exception throwable) {
            Log.i(TAG, "did not work", throwable);
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(String result) {
        Log.i(TAG, "Result passed to postExecute listActivity" + result);
        if(mOnTaskFinishedListener != null){
            mOnTaskFinishedListener.onFinished(result);
        }
    }

    public void setOnTaskFinishedListener(OnTaskFinishedListener mListener){
        mOnTaskFinishedListener = mListener;
    }
}
