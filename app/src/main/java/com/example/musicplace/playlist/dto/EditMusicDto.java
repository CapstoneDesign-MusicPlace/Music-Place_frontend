package com.example.musicplace.playlist.dto;

public class EditMusicDto {
    private Long music_id;
    private String vidioTitle;
    private String imageUrl;

    public EditMusicDto(Long music_id, String vidioTitle, String imageUrl) {
        this.music_id = music_id;
        this.vidioTitle = vidioTitle;
        this.imageUrl = imageUrl;
    }

    public Long getMusic_id() {
        return music_id;
    }

    public String getVidioTitle() {
        return vidioTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
