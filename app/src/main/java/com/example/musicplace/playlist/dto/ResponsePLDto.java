package com.example.musicplace.playlist.dto;

import com.google.gson.annotations.SerializedName;

public class ResponsePLDto {
    @SerializedName("playlist_id")
    private Long playlist_id;

    @SerializedName("pltitle")
    private String PLTitle;

    @SerializedName("cover_img")
    private String cover_img;

    @SerializedName("onOff")
    private OnOff onOff;

    @SerializedName("comment")
    private String comment;
    @SerializedName("nickname")
    private String nickname;


    public ResponsePLDto(Long playlist_id, String nickname, String PLTitle, String cover_img, OnOff onOff, String comment) {
        this.playlist_id = playlist_id;
        this.PLTitle = PLTitle;
        this.cover_img = cover_img;
        this.onOff = onOff;
        this.comment = comment;
        this.nickname = nickname;
    }

    public Long getPlaylist_id() {
        return playlist_id;
    }
    public String getNickname() {
        return nickname;
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
