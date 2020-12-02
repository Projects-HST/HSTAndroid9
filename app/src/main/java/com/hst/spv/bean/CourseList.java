package com.hst.spv.bean;

public class CourseList {

//    private String
    private String course_image;
    private String course_title_en;
    private String course_details_en;

    public CourseList(String title_image, String title, String aboutCourses) {

        this.course_image = title_image;
        this.course_title_en = title;
        this.course_details_en = aboutCourses;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }

    public String getCourse_title_en() {
        return course_title_en;
    }

    public void setCourse_title_en(String course_title_en) {
        this.course_title_en = course_title_en;
    }

    public String getCourse_details_en() {
        return course_details_en;
    }

    public void setCourse_details_en(String course_details_en) {
        this.course_details_en = course_details_en;
    }
}
