package com.rbp.server.bean;

/**
 * Created by liuwenbin on 2017/9/12.
 */
//http://sentence.iciba.com/index.php?c=dailysentence&m=getTodaySentence&_=1505130508541
public class IciDaily {

    private String content;
    private String note;
    private String picture;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
