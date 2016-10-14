package com.carepuppy.pirtu.caremypuppy.Models;

import java.util.Date;

/**
 * Created by pirtu on 23/09/2016.
 */
public class Comentario {

    private String url_img_comment;
    private String user_name_comment;
    private String content_comment;
    private Double start_comment;
    private String date_comment;

    public Comentario(String url_img_comment, String user_name_comment, String content_comment, Double start_comment, String date_comment) {
        this.url_img_comment = url_img_comment;
        this.user_name_comment = user_name_comment;
        this.content_comment = content_comment;
        this.start_comment = start_comment;
        this.date_comment = date_comment;
    }

    public  Comentario(){

    }

    public String getUrl_img_comment() {
        return url_img_comment;
    }

    public void setUrl_img_comment(String url_img_comment) {
        this.url_img_comment = url_img_comment;
    }

    public String getUser_name_comment() {
        return user_name_comment;
    }

    public void setUser_name_comment(String user_name_comment) {
        this.user_name_comment = user_name_comment;
    }

    public String getContent_comment() {
        return content_comment;
    }

    public void setContent_comment(String content_comment) {
        this.content_comment = content_comment;
    }

    public Double getStart_comment() {
        return start_comment;
    }

    public void setStart_comment(Double start_comment) {
        this.start_comment = start_comment;
    }

    public String getDate_comment() {
        return date_comment;
    }

    public void setDate_comment(String date_comment) {
        this.date_comment = date_comment;
    }
}
