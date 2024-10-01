package com.example.musicplace.playlist.dto;

public class MusicSaveDto {
    private String vidioId;

    private String vidioTitle;

    public MusicSaveDto(String vidioId, String vidioTitle) {
        this.vidioId = vidioId;
        this.vidioTitle = vidioTitle;
    }

    public String getVidioId() {
        return vidioId;
    }

    public String getVidioTitle() {
        return vidioTitle;
    }
}
