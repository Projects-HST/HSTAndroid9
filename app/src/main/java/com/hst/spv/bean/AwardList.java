package com.hst.spv.bean;

public class AwardList {

    private String id;
    private String awards_text_ta;
    private String awards_text_en;

    public AwardList(String id, String details) {

        this.id = id;
        this.awards_text_en = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAwards_text_ta() {
        return awards_text_ta;
    }

    public void setAwards_text_ta(String awards_text_ta) {
        this.awards_text_ta = awards_text_ta;
    }

    public String getAwards_text_en() {
        return awards_text_en;
    }

    public void setAwards_text_en(String awards_text_en) {
        this.awards_text_en = awards_text_en;
    }
}
