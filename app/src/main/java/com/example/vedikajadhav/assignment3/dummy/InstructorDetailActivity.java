package com.example.vedikajadhav.assignment3.dummy;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class InstructorDetailActivity extends ActionBarActivity {
    private static final String TAG = "InstructorDetailActivity";
    AndroidHttpClient mHttpClient;
    //AndroidHttpClient newHttpClient = AndroidHttpClient.newInstance(null);
    //AndroidHttpClient postHttpClient = AndroidHttpClient.newInstance(null);
    //HttpClientTask task;
   // AndroidHttpClient httpClient;

    private int mInstructorId;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mOfficeEditText;
    private EditText mPhoneEditText;
    private EditText mEmailEditText;
    private EditText mRatingEditText;
    private EditText mCommentsEditText;
    private EditText mCommentWriteText;
    private Button mPostCommentButton;
    private ListView mCommentListView;
    private CommentAdapter mCommentAdapter;
    ArrayList<Comment> mCommentsList = new ArrayList<Comment>();
    String commentToPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_detail);

        mCommentListView = (ListView)findViewById(R.id.comment_list_view);
        mFirstNameEditText = (EditText)findViewById(R.id.first_name_edit_text);
        mLastNameEditText = (EditText)findViewById(R.id.last_name_edit_text);
        /*mOfficeEditText = (EditText)findViewById(R.id.office_edit_text);
        mPhoneEditText = (EditText)findViewById(R.id.phone_edit_text);
        mEmailEditText = (EditText)findViewById(R.id.email_edit_text);
        mRatingEditText = (EditText)findViewById(R.id.rating_edit_text);*/
        //mCommentsEditText = (EditText)findViewById(R.id.comments_edit_text);
        mCommentWriteText = (EditText)findViewById(R.id.write_comment_edit_text);
        mPostCommentButton = (Button)findViewById(R.id.post_comment_button);

        mInstructorId = getIntent().getIntExtra(InstructorListActivity.EXTRA_INSTRUCTOR_ID, 0);
        /*mHttpClient = AndroidHttpClient.newInstance(null);*/
        HttpClientTask task = new HttpClientTask();
        mHttpClient = AndroidHttpClient.newInstance(null);
        String url1 = "http://bismarck.sdsu.edu/rateme/instructor/" + mInstructorId;
        String url2 = "http://bismarck.sdsu.edu/rateme/comments/" + mInstructorId;
        Log.i(TAG, "url instructor n" + url2);
        task.execute(url1, url2);
    }

    public void postComment(View postButton){
        commentToPost = mCommentWriteText.getText().toString();
        mCommentWriteText.setText("");
        Log.i(TAG, "Text inside the comment" + commentToPost);

        String postCommentUrl = "http://bismarck.sdsu.edu/rateme/comment/" + mInstructorId;
        //HttpClient httpClient = new DefaultHttpClient();
        HttpClientPostTask postTask = new HttpClientPostTask();
        mHttpClient = AndroidHttpClient.newInstance(null);
       // httpClient = AndroidHttpClient.newInstance(null);
        //mHttpClient = AndroidHttpClient.newInstance(null);
        postTask.execute(postCommentUrl);
        Log.i(TAG, "Hi");

    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(TAG, "onPause()");
        mHttpClient.close();
        //postHttpClient.close();
        //httpClient.close();
    }

    //inner HttpClientPostTask
    class HttpClientPostTask extends AsyncTask<String, Void, Void>{
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
                HttpResponse responseBody = mHttpClient.execute(postMethod);
                //Log.i(TAG,"Response body after POST COMMENT" + responseBody.toString());
            } catch (Throwable t) {
                Log.i("rew", t.toString());
            }
            return null;
        }

        @Override
        public void onPostExecute(Void result){
            //Log.i(TAG, "result passed to onPostExecute of detailActivity" + result);
            HttpClientTask newTask = new HttpClientTask();
            mHttpClient = AndroidHttpClient.newInstance(null);
            String url2 = "http://bismarck.sdsu.edu/rateme/comments/" + mInstructorId;
            newTask.execute(url2);
        }
    }

    //inner AsyncTask
    class HttpClientTask extends AsyncTask<String, Void, ArrayList<String>>{
        ArrayList<String> combinedResponse = new ArrayList<String>();
        String responseBody;
        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            try {
                for(int i = 0; i < urls.length; i++){
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    HttpGet getMethod = new HttpGet(urls[i]);
                    responseBody = mHttpClient.execute(getMethod, responseHandler);
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
            //ArrayList<Comment> mCommentsList = new ArrayList<Comment>();
            //Log.i(TAG, "result passed to onPostExecute of detailActivity" + result);
            mCommentsList.clear();
            try {
                if(result.size() == 2) {
                    JSONObject data = new JSONObject(result.get(0));
                /*Instructor newInstructor = new Instructor();
                newInstructor.setId(data.getInt("id"));
                newInstructor.setOffice(data.getString("office"));
                newInstructor.setPhone(data.getString("phone"));
                newInstructor.setEmail(data.getString("email"));
                newInstructor.setFirstName(data.getString("firstName"));
                newInstructor.setLastName(data.getString("lastName"));*/

                    mFirstNameEditText.setText(data.getString("firstName"));
                    mLastNameEditText.setText(data.getString("lastName"));
                /*mOfficeEditText.setText(data.getString("office"));
                mPhoneEditText.setText(data.getString("phone"));
                mEmailEditText.setText(data.getString("email"));*/

                    JSONArray commentsArray = new JSONArray(result.get(1));
                    for (int i = 0; i < commentsArray.length(); i++) {
                        JSONObject commentObject = (JSONObject) commentsArray.get(i);
                        Comment newComment = new Comment();
                        newComment.setCommentText(commentObject.getString("text"));
                        newComment.setCommentPostDate(commentObject.getString("date"));
                        mCommentsList.add(newComment);
                    }
                  //  Log.i(TAG, "mCommentsList" + mCommentsList);
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
                    Log.i(TAG, "mCommentsList" + mCommentsList);
                   // mCommentAdapter = new CommentAdapter(mCommentsList);
                   // mCommentListView.setAdapter(mCommentAdapter);
                    mCommentAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }

    //Inner class CommentAdapter

    private class CommentAdapter extends ArrayAdapter<Comment> {

        public CommentAdapter(ArrayList<Comment> mCommentList){
            super(getApplicationContext(), 0 ,mCommentList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //if you weren't given a view inflate one
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.comment_list_item, null);
            }

            //Configure the view for this Instructor
            Comment comment = getItem(position);

            TextView commentPostDateTextView = (TextView)convertView.findViewById(R.id.comment_date_text_view);
            commentPostDateTextView.setText(comment.getCommentPostDate());

            TextView commentTextView = (TextView)convertView.findViewById(R.id.comment_text_view);
            commentTextView.setText(comment.getCommentText());

            return convertView;
        }
    }
}
