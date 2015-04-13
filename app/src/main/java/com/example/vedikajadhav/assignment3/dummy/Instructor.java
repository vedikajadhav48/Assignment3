package com.example.vedikajadhav.assignment3.dummy;

/**
 * Created by Vedika Jadhav on 3/18/2015.
 */
public class Instructor {
    private int mId;
    private String mFirstName;
    private String mLastName;
    private String mOffice;
    private String mPhone;
    private String mEmail;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    @Override
    public String toString(){
        return mFirstName;
    }
}
