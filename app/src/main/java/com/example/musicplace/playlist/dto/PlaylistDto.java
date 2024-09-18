package com.example.musicplace.playlist.dto;

public class PlaylistDto {
    private String playlistTitle;  // 플레이리스트 제목
    private String creatorName;    // 플레이리스트 생성자
    private String imageUrl;       // 이미지 URL 또는 로컬 이미지 경로

    // 생성자
    public PlaylistDto(String playlistTitle, String creatorName, String imageUrl) {
        this.playlistTitle = playlistTitle;
        this.creatorName = creatorName;
        this.imageUrl = imageUrl;
    }

    // Getter 및 Setter
    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}