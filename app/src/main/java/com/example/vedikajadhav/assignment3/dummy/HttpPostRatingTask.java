package com.example.vedikajadhav.assignment3.dummy;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;

/**
 * Created by Vedika Jadhav on 3/22/2015.
 */
public class HttpPostRatingTask extends AsyncTask<String, Void, String>{
    private static final String TAG = "HttpPostRatingTask";
    OnTaskFinishedListener mOnTaskFinishedListener;
    AndroidHttpClient mAndroidHttpClient;
    private Context mContext;
    String mRating;

    public HttpPostRatingTask(AndroidHttpClient instructorDetailHttpClient, Context context, String selectedRate) {
        mAndroidHttpClient = instructorDetailHttpClient;
        mContext = context;
        mOnTaskFinishedListener = (OnTaskFinishedListener)mContext;
        mRating = selectedRate;
    }

    @Override
    protected String doInBackground(String... postUrl) {
        HttpPost postMethod = new HttpPost(postUrl[0]);
        StringEntity rating = null;
        Log.i(TAG, "mRating inside PostTask" + mRating);
        try {
            rating = new StringEntity(mRating, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            Log.i("rew", e.toString());
        }
        postMethod.setHeader("Content-Type", "application/json;charset=UTF-8");
        postMethod.setEntity(rating);
        try {
            HttpResponse responseBody = mAndroidHttpClient.execute(postMethod);
        } catch (Throwable t) {
            Log.i("rew", t.toString());
        }
        return null;
    }

    @Override
    public void onPostExecute(String result) {
        Toast.makeText(mContext, "Rating posted", Toast.LENGTH_SHORT).show();
       /* if (mOnTaskFinishedListener != null) {
            mOnTaskFinishedListener.onRatingFinished(result);
        }*/
    }

    public void setOnTaskFinishedListener(OnTaskFinishedListener mListener){
        mOnTaskFinishedListener = mListener;
    }
}
