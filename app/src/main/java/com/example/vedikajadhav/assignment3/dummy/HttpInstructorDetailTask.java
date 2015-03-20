package com.example.vedikajadhav.assignment3.dummy;

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
public class HttpInstructorDetailTask {
/*    ArrayList<String> combinedResponse = new ArrayList<String>();
    String responseBody;
    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        try {
            for(int i = 0; i < urls.length; i++){
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpGet getMethod = new HttpGet(urls[i]);
                responseBody = mInstructorDetailHttpClient.execute(getMethod, responseHandler);
                combinedResponse.add(responseBody);
            }
            Log.i(TAG, "Combined response" + combinedResponse);
            return combinedResponse;
        } catch (Throwable throwable) {
            Log.i(TAG, "did not work", throwable);
        }
        return null;
    }

    @Override
    public void onPostExecute(ArrayList<String> result){
        mCommentsList.clear();
        try {
            if(result.size() == 2) {
                JSONObject data = new JSONObject(result.get(0));
                mOfficeEditText.setText(data.getString("office"));
                mPhoneEditText.setText(data.getString("phone"));
                mEmailEditText.setText(data.getString("email"));

                JSONArray commentsArray = new JSONArray(result.get(1));
                for (int i = 0; i < commentsArray.length(); i++) {
                    JSONObject commentObject = (JSONObject) commentsArray.get(i);
                    Comment newComment = new Comment();
                    newComment.setCommentText(commentObject.getString("text"));
                    newComment.setCommentPostDate(commentObject.getString("date"));
                    mCommentsList.add(newComment);
                }
                mCommentAdapter = new CommentAdapter(mCommentsList);
                mCommentListView.setAdapter(mCommentAdapter);
            }
            else{
                Log.i(TAG, "Inside else" + result.size());
                JSONArray commentsArray = new JSONArray(result.get(0));
                Log.i(TAG, "JSONArray comments" + commentsArray);
                for (int i = 0; i < commentsArray.length(); i++) {
                    JSONObject commentObject = (JSONObject) commentsArray.get(i);
                    Comment newComment = new Comment();
                    newComment.setCommentText(commentObject.getString("text"));
                    newComment.setCommentPostDate(commentObject.getString("date"));
                    mCommentsList.add(newComment);
                }
                mCommentAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }*/
}
