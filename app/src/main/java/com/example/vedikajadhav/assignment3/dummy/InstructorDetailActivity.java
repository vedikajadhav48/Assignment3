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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    HttpPostRatingTask mPostRatingTask;
    String mInstructorDetailUrl, mInstructorCommentsUrl, mPostCommentUrl, mPostRateUrl;
    private int mInstructorId;
    private String mInstructorFirstName;
    private String mInstructorLastName;
    private EditText mOfficeEditText;
    private EditText mPhoneEditText;
    private EditText mEmailEditText;
    private EditText mRatingEditText;
    private RadioGroup mRateRadioGroup;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private RadioButton mRadioButton3;
    private RadioButton mRadioButton4;
    private RadioButton mRadioButton5;
    private String selectedRate;
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
        mRateRadioGroup = (RadioGroup)findViewById(R.id.radioGroup1);
        mRadioButton1 = (RadioButton)findViewById(R.id.radio0);
        mRadioButton2 = (RadioButton)findViewById(R.id.radio1);
        mRadioButton3 = (RadioButton)findViewById(R.id.radio2);
        mRadioButton4 = (RadioButton)findViewById(R.id.radio3);
        mRadioButton5 = (RadioButton)findViewById(R.id.radio4);
        mRadioButton1.setSelected(true);
        mRateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i){
                if(i == R.id.radio0){
                    selectedRate = mRadioButton1.getText().toString();
                }else if (i == R.id.radio1){
                    selectedRate = mRadioButton2.getText().toString();
                }else if (i == R.id.radio2){
                    selectedRate = mRadioButton3.getText().toString();
                }else if (i == R.id.radio3){
                    selectedRate = mRadioButton4.getText().toString();
                }else{
                    selectedRate = mRadioButton5.getText().toString();
                }
            }
        });
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

    public void rateInstructor(View rateButton){
        mPostRateUrl = (getResources().getString(R.string.post_instructor_rating_url)) + selectedRate + "/" + mInstructorId;
        mInstructorDetailHttpClient = AndroidHttpClient.newInstance(null);
        mPostRatingTask = new HttpPostRatingTask(mInstructorDetailHttpClient, this, selectedRate);
        mPostRatingTask.execute(mPostRateUrl);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        if(mInstructorDetailHttpClient != null){
            mInstructorDetailHttpClient.close();
        }
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
                mRatingEditText.setText("average:" + data.getJSONObject("rating").getString("average")
                        + " total Rating:" + data.getJSONObject("rating").getString("totalRatings"));

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
        //mCommentsUrl = (mContext.getResources().getString(R.string.instructor_detail_url)) + mInstructorId;
        mInstructorDetailHttpClient = AndroidHttpClient.newInstance(null);
        HttpInstructorDetailTask newGetCommentsTask = new HttpInstructorDetailTask(mInstructorDetailHttpClient, this);
        newGetCommentsTask.execute(mInstructorCommentsUrl);
    }

    @Override
    public void onDetailedFinished(ArrayList<String> result) {
        getInstructorDetails(result);
    }

    @Override
    public void onRatingFinished(String result) {
        mInstructorDetailHttpClient = AndroidHttpClient.newInstance(null);
        HttpInstructorDetailTask newGetRatingTask = new HttpInstructorDetailTask(mInstructorDetailHttpClient, this);
        newGetRatingTask.execute(mInstructorDetailUrl);
    }
}
