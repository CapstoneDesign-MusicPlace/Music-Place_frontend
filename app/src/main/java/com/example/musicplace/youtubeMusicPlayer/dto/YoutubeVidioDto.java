package com.example.musicplace.youtubeMusicPlayer.dto;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

// YoutubeVidioDto 클래스에 vidioImage 파싱 로직 추가
public class YoutubeVidioDto{
    @SerializedName("vidioId")
    private String vidioId;
    @SerializedName("vidioTitle")
    private String vidioTitle;
    @SerializedName("vidioImage")
    private String vidioImage;  // 문자열로 인코딩된 JSON

    // vidioImage를 실제 객체로 변환하는 메서드 추가
    public VidioImage getParsedVidioImage() {
        Gson gson = new Gson();
        try {
            // vidioImage를 VidioImage 객체로 변환
            return gson.fromJson(vidioImage, VidioImage.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;  // 파싱 실패 시 null 반환
        }
    }

    // 기존 getter와 setter
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