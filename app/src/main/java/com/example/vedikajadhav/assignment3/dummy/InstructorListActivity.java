package com.example.vedikajadhav.assignment3.dummy;

import android.app.ListActivity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vedikajadhav.assignment3.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class InstructorListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "InstructorListActivity";
    private ListView mListView;
    private InstructorAdapter instructorAdapter;
    AndroidHttpClient mInstructorListHttpClient;
    HttpInstructorListTask mInstructorListTask = new HttpInstructorListTask();
    private ArrayList<Instructor> mInstructorNamesList = new ArrayList<Instructor>();
    public static final String EXTRA_INSTRUCTOR_ID = "com.example.vedikajadhav48.assignment3.instructorDetailActivityIntent.instructorID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        mListView = (ListView)findViewById(R.id.instructor_list_view);
        mInstructorListHttpClient = AndroidHttpClient.newInstance(null);
        String url = "http://bismarck.sdsu.edu/rateme/list";
        Log.i(TAG, "onCreate listACtvity" + url);
        mInstructorListTask.execute(url);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        //String userAgent = null;
        /*mHttpClient = AndroidHttpClient.newInstance(userAgent);
        String url = "http://bismarck.sdsu.edu/rateme/list";
        task.execute(url);*/
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(TAG, "onPause()");
        mInstructorListHttpClient.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG,"Item clicked" + position);
        Instructor instructor = (Instructor)parent.getAdapter().getItem(position);
        Intent intent = new Intent(this, InstructorDetailActivity.class);
        intent.putExtra(EXTRA_INSTRUCTOR_ID, instructor.getId());
        startActivity(intent);
    }

    class HttpInstructorListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpGet getMethod = new HttpGet(urls[0]);
                String responseBody = mInstructorListHttpClient.execute(getMethod, responseHandler);
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
            try {
                JSONArray data = new JSONArray(result);
                for(int i=0; i<data.length(); i++){
                    JSONObject firstPerson = (JSONObject) data.get(i);
                    Instructor newInstructor = new Instructor();
                    newInstructor.setId(firstPerson.getInt("id"));
                    newInstructor.setFirstName(firstPerson.getString("firstName"));
                    newInstructor.setLastName(firstPerson.getString("lastName"));
                    mInstructorNamesList.add(newInstructor);
                }
                Log.i(TAG, "mInstructorNamesList" + mInstructorNamesList);
                instructorAdapter = new InstructorAdapter(mInstructorNamesList);
                mListView.setAdapter(instructorAdapter);
                //listAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Inner class InstructorAdapter

    private class InstructorAdapter extends ArrayAdapter<Instructor> {

        public InstructorAdapter(ArrayList<Instructor> mInstructorNamesList){
            super(getApplicationContext(), 0 ,mInstructorNamesList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //if you weren't given a view inflate one
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.instructor_list_item, null);
            }

            //Configure the view for this Instructor
            Instructor instructor = getItem(position);

            TextView firstNameTextView = (TextView)convertView.findViewById(R.id.instructor_firstName_textView);
            firstNameTextView.setText(instructor.getFirstName());

            TextView lastNameTextView = (TextView)convertView.findViewById(R.id.instructor_lastName_textView);
            lastNameTextView.setText(instructor.getLastName());

            return convertView;
        }

    }
}
