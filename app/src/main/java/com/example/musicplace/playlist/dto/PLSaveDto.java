package com.example.musicplace.playlist.dto;

public class PLSaveDto {
    private String title;

    private OnOff onOff;

    private String cover_img;

    private String comment;

    public PLSaveDto(String title, OnOff onOff, String cover_img, String comment) {
        this.title = title;
        this.onOff = onOff;
        this.cover_img = cover_img;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public OnOff getOnOff() {
        return onOff;
    }

    public String getCover_img() {
        return cover_img;
    }

    public String getComment() {
        return comment;
    }
}
