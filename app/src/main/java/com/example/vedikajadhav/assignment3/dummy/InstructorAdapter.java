package com.example.vedikajadhav.assignment3.dummy;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vedikajadhav.assignment3.R;

import java.util.ArrayList;

/**
 * Created by Vedika Jadhav on 3/20/2015.
 */
public class InstructorAdapter extends ArrayAdapter<Instructor> {
    private Context mContext;

    public InstructorAdapter(ArrayList<Instructor> mInstructorNamesList, Context context){
        super(context.getApplicationContext(), 0 ,mInstructorNamesList);
        mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //if you weren't given a view inflate one
        if(convertView == null){
            convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.instructor_list_item, null);
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
