package com.example.musicplace.follow.dto;

public class FollowSaveDto {
    private String target_id;

    private String profile_img_url;

    private String nickname;

    public FollowSaveDto(String target_id, String profile_img_url, String nickname) {
        this.target_id = target_id;
        this.profile_img_url = profile_img_url;
        this.nickname = nickname;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTarget_id() {
        return target_id;
    }
}
