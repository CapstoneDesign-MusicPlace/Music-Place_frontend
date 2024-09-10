package com.example.musicplace.dto.youtub;

import com.example.musicplace.dto.youtub.ImageQuality;
import com.google.gson.annotations.SerializedName;

public class VidioImage {
    @SerializedName("default")
    private ImageQuality defaultQuality;
    @SerializedName("medium")
    private ImageQuality mediumQuality;
    @SerializedName("high")
    private ImageQuality highQuality;

    // 게터와 세터
    public ImageQuality getDefaultQuality() {
        return defaultQuality;
    }

    public void setDefaultQuality(ImageQuality defaultQuality) {
        this.defaultQuality = defaultQuality;
    }

    public ImageQuality getMediumQuality() {
        return mediumQuality;
    }

    public void setMediumQuality(ImageQuality mediumQuality) {
        this.mediumQuality = mediumQuality;
    }

    public ImageQuality getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(ImageQuality highQuality) {
        this.highQuality = highQuality;
    }
}