package com.example.musicplace.youtubeMusicPlayer.dto;

public class LoadPlaylistDto {
    private Long playlistId;
    private String playlistTitle;
    private String imageUrl;

    public LoadPlaylistDto(Long playlistId, String playlistTitle, String imageUrl) {
        this.playlistId = playlistId;
        this.playlistTitle = playlistTitle;
        this.imageUrl = imageUrl;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
