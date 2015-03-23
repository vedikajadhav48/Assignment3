package com.example.vedikajadhav.assignment3.dummy;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vedikajadhav.assignment3.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;

/**
 * Created by Vedika Jadhav on 3/20/2015.
 */
public class HttpPostCommentTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "HttpPostCommentTask";
    OnTaskFinishedListener mOnTaskFinishedListener;
    AndroidHttpClient mAndroidHttpClient;
    private Context mContext;
    String mComment;
    String mCommentsUrl;
    int mInstructorId;

    public HttpPostCommentTask(AndroidHttpClient instructorDetailHttpClient, Context context, String commentToPost, int mInstructorId) {
        mAndroidHttpClient = instructorDetailHttpClient;
        mContext = context;
        mOnTaskFinishedListener = (OnTaskFinishedListener)mContext;
        mComment = commentToPost;
        mInstructorId = mInstructorId;
    }


    @Override
    protected String doInBackground(String... postUrl) {
        HttpPost postMethod = new HttpPost(postUrl[0]);
        StringEntity comment = null;
        Log.i(TAG, "mComment inside PostTask" + mComment);
        try {
            comment = new StringEntity(mComment, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            Log.i("rew", e.toString());
        }
        postMethod.setHeader("Content-Type", "application/json;charset=UTF-8");
        postMethod.setEntity(comment);
        try {
            HttpResponse responseBody = mAndroidHttpClient.execute(postMethod);
        } catch (Throwable t) {
            Log.i("rew", t.toString());
        }
        return null;
    }

    @Override
    public void onPostExecute(String result) {
        Log.i(TAG, "postComment result" + result);
        if (mOnTaskFinishedListener != null) {
            mOnTaskFinishedListener.onFinished(result);
        }
            /*mCommentsUrl = (mContext.getResources().getString(R.string.instructor_detail_url)) + mInstructorId;
            mAndroidHttpClient = AndroidHttpClient.newInstance(null);
            HttpInstructorDetailTask newGetCommentsTask = new HttpInstructorDetailTask(mAndroidHttpClient, mContext);
            newGetCommentsTask.execute(mCommentsUrl);*/


/*            String url2 = "http://bismarck.sdsu.edu/rateme/comments/" + mInstructorId;
            newTask.execute(url2);*/
    }

    public void setOnTaskFinishedListener(OnTaskFinishedListener mListener){
        mOnTaskFinishedListener = mListener;
    }
}
