package com.hst.spv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsFeed implements Serializable {

    @SerializedName("newsfeed_id")
    @Expose
    private String id;

    @SerializedName("nf_profile_type")
    @Expose
    private String profileType;

    @SerializedName("title_ta")
    @Expose
    private String titleTamil;

    @SerializedName("title_en")
    @Expose
    private String titleEnglish;

    @SerializedName("description_ta")
    @Expose
    private String descriptionTamil;

    @SerializedName("description_en")
    @Expose
    private String descriptionEnglish;

    @SerializedName("news_date")
    @Expose
    private String newsDate;

    @SerializedName("nf_cover_image")
    @Expose
    private String coverImage;

    @SerializedName("nf_video_token_id")
    @Expose
    private String videoTokenId;

    @SerializedName("gallery_status")
    @Expose
    private String galleryStatus;

    @SerializedName("view_count")
    @Expose
    private String viewCount;

    @SerializedName("likes_count")
    @Expose
    private String likesCount;

    @SerializedName("share_count")
    @Expose
    private String shareCount;

    @SerializedName("comments_count")
    @Expose
    private String commentCount;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("like_status")
    @Expose
    private String likeStatus;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The profileType
     */
    public String getProfileType() {
        return profileType;
    }

    /**
     * @param profileType The profileType
     */
    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    /**
     * @return The titleTamil
     */
    public String getTitleTamil() {
        return titleTamil;
    }

    /**
     * @param titleTamil The titleTamil
     */
    public void setTitleTamil(String titleTamil) {
        this.titleTamil = titleTamil;
    }

    /**
     * @return The descriptionTamil
     */
    public String getDescriptionTamil() {
        return descriptionTamil;
    }

    /**
     * @param descriptionTamil The descriptionTamil
     */
    public void setDescriptionTamil(String descriptionTamil) {
        this.descriptionTamil = descriptionTamil;
    }

    /**
     * @return The descriptionEnglish
     */
    public String getDescriptionEnglish() {
        return descriptionEnglish;
    }

    /**
     * @param descriptionEnglish The descriptionEnglish
     */
    public void setDescriptionEnglish(String descriptionEnglish) {
        this.descriptionEnglish = descriptionEnglish;
    }

    /**
     * @return The titleEnglish
     */
    public String getTitleEnglish() {
        return titleEnglish;
    }

    /**
     * @param titleEnglish The titleEnglish
     */
    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    /**
     * @return The newsDate
     */
    public String getNewsDate() {
        return newsDate;
    }

    /**
     * @param newsDate The newsDate
     */
    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    /**
     * @return The coverImage
     */
    public String getCoverImage() {
        return coverImage;
    }

    /**
     * @param coverImage The coverImage
     */
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    /**
     * @return The videoTokenId
     */
    public String getVideoTokenId() {
        return videoTokenId;
    }

    /**
     * @param videoTokenId The videoTokenId
     */
    public void setVideoTokenId(String videoTokenId) {
        this.videoTokenId = videoTokenId;
    }

    /**
     * @return The galleryStatus
     */
    public String getGalleryStatus() {
        return galleryStatus;
    }

    /**
     * @param galleryStatus The galleryStatus
     */
    public void setGalleryStatus(String galleryStatus) {
        this.galleryStatus = galleryStatus;
    }

    /**
     * @return The viewCount
     */
    public String getViewCount() {
        return viewCount;
    }

    /**
     * @param viewCount The viewCount
     */
    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * @return The likesCount
     */
    public String getLikesCount() {
        return likesCount;
    }

    /**
     * @param likesCount The likesCount
     */
    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    /**
     * @return The shareCount
     */
    public String getShareCount() {
        return shareCount;
    }

    /**
     * @param shareCount The shareCount
     */
    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    /**
     * @return The commentCount
     */
    public String getCommentCount() {
        return commentCount;
    }

    /**
     * @param commentCount The commentCount
     */
    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The likeStatus
     */
    public String getLikeStatus() {
        return likeStatus;
    }

    /**
     * @param likeStatus The likeStatus
     */
    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }

}