package com.example.vedikajadhav.assignment3.dummy;

import java.util.ArrayList;

/**
 * Created by Vedika Jadhav on 3/20/2015.
 */
public interface OnTaskFinishedListener {
    public void onFinished(String result);
    public void onDetailedFinished(ArrayList<String> result);
    public void onRatingFinished(String result);
}
