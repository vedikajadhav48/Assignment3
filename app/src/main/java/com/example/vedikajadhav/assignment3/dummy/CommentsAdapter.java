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
public class CommentsAdapter extends ArrayAdapter<Comment> {
    private Context mContext;

    public CommentsAdapter(ArrayList<Comment> mCommentList, Context context) {
        super(context.getApplicationContext(), 0, mCommentList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //if you weren't given a view inflate one
        if (convertView == null) {
            convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.comment_list_item, null);
        }

        //Configure the view for this Instructor
        Comment comment = getItem(position);

        TextView commentPostDateTextView = (TextView) convertView.findViewById(R.id.comment_date_text_view);
        commentPostDateTextView.setText(comment.getCommentPostDate());

        TextView commentTextView = (TextView) convertView.findViewById(R.id.comment_text_view);
        commentTextView.setText(comment.getCommentText());

        return convertView;
    }
}
