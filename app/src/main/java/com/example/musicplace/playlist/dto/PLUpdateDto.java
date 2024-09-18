package com.example.musicplace.playlist.dto;

public class PLUpdateDto {
    private OnOff onOff;

    private String cover_img;

    private String comment;

    public PLUpdateDto(OnOff onOff, String cover_img, String comment) {
        this.onOff = onOff;
        this.cover_img = cover_img;
        this.comment = comment;
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
