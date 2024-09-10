package com.example.musicplace.dto.youtub;

public class YoutubeItem {
    private String vidioTitle;
    private String imageUrl;

    public YoutubeItem(String imageUrl, String vidioTitle) {
        this.vidioTitle = vidioTitle;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVidioTitle() {
        return vidioTitle;
    }

    public void setVidioTitle(String vidioTitle) {
        this.vidioTitle = vidioTitle;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}