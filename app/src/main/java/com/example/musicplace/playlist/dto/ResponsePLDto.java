package com.example.musicplace.playlist.dto;

public class ResponsePLDto {
    private Long playlist_id;

    private String PLTitle;

    private String cover_img;

    private OnOff onOff;

    private String comment;

    public ResponsePLDto(Long playlist_id, String PLTitle, String cover_img, OnOff onOff, String comment) {
        this.playlist_id = playlist_id;
        this.PLTitle = PLTitle;
        this.cover_img = cover_img;
        this.onOff = onOff;
        this.comment = comment;
    }

    public Long getPlaylist_id() {
        return playlist_id;
    }

    public String getPLTitle() {
        return PLTitle;
    }

    public String getCover_img() {
        return cover_img;
    }

    public OnOff getOnOff() {
        return onOff;
    }

    public String getComment() {
        return comment;
    }
}
