package com.hst.spv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GalleryImageList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("nf_result")
    @Expose
    private ArrayList<Gallery> galleryArrayList = new ArrayList<>();

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
     * @return The galleryArrayList
     */
    public ArrayList<Gallery> getGalleryArrayList() {
        return galleryArrayList;
    }

    /**
     * @param galleryArrayList The galleryArrayList
     */
    public void setGalleryArrayList(ArrayList<Gallery> galleryArrayList) {
        this.galleryArrayList = galleryArrayList;
    }
}