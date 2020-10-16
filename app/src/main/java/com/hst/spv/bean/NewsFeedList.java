package com.hst.spv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsFeedList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("nf_result")
    @Expose
    private ArrayList<NewsFeed> newsFeedArrayList = new ArrayList<>();

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The newsFeedArrayList
     */
    public ArrayList<NewsFeed> getNewsFeedArrayList() {
        return newsFeedArrayList;
    }

    /**
     * @param newsFeedArrayList The newsFeedArrayList
     */
    public void setNewsFeedArrayList(ArrayList<NewsFeed> newsFeedArrayList) {
        this.newsFeedArrayList = newsFeedArrayList;
    }
}