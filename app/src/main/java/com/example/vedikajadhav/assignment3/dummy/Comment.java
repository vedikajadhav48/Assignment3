package com.example.vedikajadhav.assignment3.dummy;

/**
 * Created by Vedika Jadhav on 3/18/2015.
 */
public class Comment {
    private String mCommentText;
    private String mCommentPostDate;

    public String getCommentText() {
        return mCommentText;
    }

    public void setCommentText(String commentText) {
        mCommentText = commentText;
    }

    public String getCommentPostDate() {
        return mCommentPostDate;
    }

    public void setCommentPostDate(String commentPostDate) {
        mCommentPostDate = commentPostDate;
    }

    @Override
    public String toString(){
        return mCommentText;
    }
}
