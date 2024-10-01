package com.example.musicplace.playlist.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseMusicDto {

    @SerializedName("music_id")
    private Long music_id;

    @SerializedName("vidioId")
    private String vidioId;

    @SerializedName("vidioTitle")
    private String vidioTitle;
    @SerializedName("vidioImage")
    private String vidioImage;

    public ResponseMusicDto(Long music_id, String vidioId, String vidioTitle, String vidioImage) {
        this.music_id = music_id;
        this.vidioId = vidioId;
        this.vidioTitle = vidioTitle;
        this.vidioImage = vidioImage;
    }

    public Long getMusic_id() {
        return music_id;
    }

    public String getVidioId() {
        return vidioId;
    }

    public String getVidioTitle() {
        return vidioTitle;
    }

    public String getVidioImage() {
        return vidioImage;
    }
}
