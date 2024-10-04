package com.example.musicplace.playlist.dto;

public class MusicSaveDto {
    private String vidioId;

    private String vidioTitle;
    private String vidioImage;

    public MusicSaveDto(String vidioId, String vidioTitle, String vidioImage) {
        this.vidioId = vidioId;
        this.vidioTitle = vidioTitle;
        this.vidioImage = vidioImage;
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
