package com.example.musicplace.main.dto;

public class FollowResponseDto {
    private Long follow_id;
    private String target_id;

    public FollowResponseDto(Long follow_id, String target_id) {
        this.follow_id = follow_id;
        this.target_id = target_id;
    }

    public Long getFollow_id() {
        return follow_id;
    }

    public String getTarget_id() {
        return target_id;
    }
}
