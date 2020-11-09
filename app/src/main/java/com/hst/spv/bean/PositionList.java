package com.hst.spv.bean;

public class PositionList {

    private String id;
    private String title_en;
    private String position_text_en;

    public PositionList(String id, String title_en, String position_text_en) {
        this.id = id;
        this.title_en = title_en;
        this.position_text_en = position_text_en;
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

    public String getPosition_text_en() {
        return position_text_en;
    }

    public void setPosition_text_en(String position_text_en) {
        this.position_text_en = position_text_en;
    }
}
