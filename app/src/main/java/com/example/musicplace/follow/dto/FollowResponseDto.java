package com.example.musicplace.follow.dto;

public class FollowResponseDto {
    private Long follow_id;
    private String target_id;
    private String profile_img_url;
    private String nickname;

    public FollowResponseDto(Long follow_id, String target_id, String profile_img_url, String nickname) {
        this.follow_id = follow_id;
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

    public Long getFollow_id() {
        return follow_id;
    }

    public String getTarget_id() {
        return target_id;
    }
}
