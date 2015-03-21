package com.example.vedikajadhav.assignment3.dummy;

import android.app.ListActivity;
import android.content.Context;
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

public class InstructorListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, OnTaskFinishedListener {
    private static final String TAG = "InstructorListActivity";
    private ListView mListView;
    private InstructorAdapter instructorAdapter;
    AndroidHttpClient mInstructorListHttpClient;
    HttpInstructorListTask mInstructorListTask;
    private ArrayList<Instructor> mInstructorNamesList = new ArrayList<Instructor>();
    public static final String EXTRA_INSTRUCTOR_ID = "com.example.vedikajadhav48.assignment3.instructorDetailActivityIntent.instructorID";
    public static final String EXTRA_INSTRUCTOR_FIRST_NAME = "com.example.vedikajadhav48.assignment3.instructorDetailActivityIntent.instructorFirstName";
    public static final String EXTRA_INSTRUCTOR_LAST_NAME = "com.example.vedikajadhav48.assignment3.instructorDetailActivityIntent.instructorLastName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        mListView = (ListView)findViewById(R.id.instructor_list_view);
        instructorAdapter = new InstructorAdapter(mInstructorNamesList, this);

        Object object = getLastCustomNonConfigurationInstance();

        if(null != object){
            instructorAdapter = (InstructorAdapter)object;
        }
        else{
            mInstructorListHttpClient = AndroidHttpClient.newInstance(null);
            mInstructorListTask = new HttpInstructorListTask(mInstructorListHttpClient, this);
            mInstructorListTask.execute(getResources().getString(R.string.instructor_list_url));
            Log.i(TAG, "vedika mInstructorNamesList" + mInstructorNamesList);
        }
        mListView.setAdapter(instructorAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return instructorAdapter;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(TAG, "onPause()");
        if(mInstructorListHttpClient != null){
            mInstructorListHttpClient.close();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG,"Item clicked" + position);
        Instructor instructor = (Instructor)parent.getAdapter().getItem(position);
        Intent intent = new Intent(this, InstructorDetailActivity.class);
        intent.putExtra(EXTRA_INSTRUCTOR_ID, instructor.getId());
        intent.putExtra(EXTRA_INSTRUCTOR_FIRST_NAME, instructor.getFirstName());
        intent.putExtra(EXTRA_INSTRUCTOR_LAST_NAME, instructor.getLastName());
        startActivity(intent);
    }

    public void getInstructorList(String result){
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

            Log.i(TAG, "getInstructorList");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinished(String result) {
        getInstructorList(result);
    }

    @Override
    public void onDetailedFinished(ArrayList<String> result) {

    }
}
