package com.example.musicplace.youtubeMusicPlayer.youtubeDto;

public class playVidioDto {
    private String vidioId;
    private String vidioTitle;
    private String vidioImage; // 이 필드는 JSON에서 문자열 형태로 되어 있음

    // Getter 및 Setter 메서드
    public String getVidioId() {
        return vidioId;
    }

    public void setVidioId(String vidioId) {
        this.vidioId = vidioId;
    }

    public String getVidioTitle() {
        return vidioTitle;
    }

    public void setVidioTitle(String vidioTitle) {
        this.vidioTitle = vidioTitle;
    }

    public String getVidioImage() {
        return vidioImage;
    }

    public void setVidioImage(String vidioImage) {
        this.vidioImage = vidioImage;
    }
}
