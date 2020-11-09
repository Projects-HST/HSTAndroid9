package com.hst.spv.bean;

public class NotableList {

    private String id;
    private String title_en;
    private String noteable_text_en;

    public NotableList(String id, String title_en, String noteable_text_en) {
        this.id = id;
        this.title_en = title_en;
        this.noteable_text_en = noteable_text_en;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getNoteable_text_en() {
        return noteable_text_en;
    }

    public void setNoteable_text_en(String noteable_text_en) {
        this.noteable_text_en = noteable_text_en;
    }
}
