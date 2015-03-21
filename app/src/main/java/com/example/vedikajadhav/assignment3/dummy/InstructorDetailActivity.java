package com.example.vedikajadhav.assignment3.dummy;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vedikajadhav.assignment3.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class InstructorDetailActivity extends ActionBarActivity implements OnTaskFinishedListener{
    private static final String TAG = "InstructorDetailActivity";
    AndroidHttpClient mInstructorDetailHttpClient;
    HttpInstructorDetailTask mInstructorDetailTask;
    HttpPostCommentTask mPostCommentTask;
    String mInstructorDetailUrl, mInstructorCommentsUrl, mPostCommentUrl;
    private int mInstructorId;
    private String mInstructorFirstName;
    private String mInstructorLastName;
    private EditText mOfficeEditText;
    private EditText mPhoneEditText;
    private EditText mEmailEditText;
    private EditText mRatingEditText;
    private EditText mCommentsEditText;
    private EditText mCommentWriteText;
    private ListView mCommentListView;
    private CommentsAdapter mCommentAdapter;
    ArrayList<Comment> mCommentsList = new ArrayList<Comment>();
    ArrayList<String> combinedResponse = new ArrayList<String>();
    String commentToPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_detail);

        Log.i(TAG, "onCreate()");
        mCommentListView = (ListView) findViewById(R.id.comment_list_view);
        mOfficeEditText = (EditText) findViewById(R.id.office_edit_text);
        mPhoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mRatingEditText = (EditText) findViewById(R.id.rating_edit_text);
        //mCommentsEditText = (EditText)findViewById(R.id.comments_edit_text);
        mCommentWriteText = (EditText) findViewById(R.id.write_comment_edit_text);

        mCommentAdapter = new CommentsAdapter(mCommentsList, this);

        mInstructorId = getIntent().getIntExtra(InstructorListActivity.EXTRA_INSTRUCTOR_ID, 0);
        mInstructorFirstName = getIntent().getStringExtra(InstructorListActivity.EXTRA_INSTRUCTOR_FIRST_NAME);
        mInstructorLastName = getIntent().getStringExtra(InstructorListActivity.EXTRA_INSTRUCTOR_LAST_NAME);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setSubtitle(mInstructorFirstName + " " + mInstructorLastName);
        }

        mInstructorDetailUrl = (getResources().getString(R.string.instructor_detail_url)) + mInstructorId;
        mInstructorCommentsUrl = (getResources().getString(R.string.instructor_comments_url)) + mInstructorId;
        mInstructorDetailHttpClient = AndroidHttpClient.newInstance(null);
        mInstructorDetailTask = new HttpInstructorDetailTask(mInstructorDetailHttpClient, this);
        mInstructorDetailTask.execute(mInstructorDetailUrl, mInstructorCommentsUrl);
        mCommentListView.setAdapter(mCommentAdapter);
    }

    public void postComment(View postButton) {
        commentToPost = mCommentWriteText.getText().toString();
        mCommentWriteText.setText("");
        Log.i(TAG, "Text inside the comment" + commentToPost);

        mPostCommentUrl = (getResources().getString(R.string.post_instructor_comment_url)) + mInstructorId;
        mInstructorDetailHttpClient = AndroidHttpClient.newInstance(null);
        mPostCommentTask = new HttpPostCommentTask(mInstructorDetailHttpClient, this, commentToPost);
        mPostCommentTask.execute(mPostCommentUrl);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        mInstructorDetailHttpClient.close();
    }

    public void getInstructorComments(String commentResult) {
        mCommentsList.clear();
        JSONArray commentsArray = null;
        try {
            commentsArray = new JSONArray(commentResult);

            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject commentObject = (JSONObject) commentsArray.get(i);
                Comment newComment = new Comment();
                newComment.setCommentText(commentObject.getString("text"));
                newComment.setCommentPostDate(commentObject.getString("date"));
                mCommentsList.add(newComment);
            }
            mCommentAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getInstructorDetails(ArrayList<String> result) {
        try {
            if (result.size() == 2) {
                JSONObject data = new JSONObject(result.get(0));
                mOfficeEditText.setText(data.getString("office"));
                mPhoneEditText.setText(data.getString("phone"));
                mEmailEditText.setText(data.getString("email"));

                getInstructorComments(result.get(1));
            } else {
                getInstructorComments(result.get(0));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFinished(String result) {

    }

    @Override
    public void onDetailedFinished(ArrayList<String> result) {
        getInstructorDetails(result);
    }

    //inner HttpClientPostTask
 /*   class HttpClientPostTask extends AsyncTask<String, Void, Void> {
        String responseBody;

        @Override
        protected Void doInBackground(String... postUrl) {
            HttpPost postMethod = new HttpPost(postUrl[0]);
            StringEntity comment = null;
            try {
                comment = new StringEntity(commentToPost, HTTP.UTF_8);
            } catch (UnsupportedEncodingException e) {
                Log.i("rew", e.toString());
            }
            postMethod.setHeader("Content-Type", "application/json;charset=UTF-8");
            postMethod.setEntity(comment);
            try {
                HttpResponse responseBody = mInstructorDetailHttpClient.execute(postMethod);
            } catch (Throwable t) {
                Log.i("rew", t.toString());
            }
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            *//*mInstructorDetailHttpClient = AndroidHttpClient.newInstance(null);
            HttpInstructorDetailTask newTask = new HttpInstructorDetailTask(mInstructorDetailHttpClient, this);
            String url2 = "http://bismarck.sdsu.edu/rateme/comments/" + mInstructorId;
            newTask.execute(url2);*//*
        }
    }*/

}
