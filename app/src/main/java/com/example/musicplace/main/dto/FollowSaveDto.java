package com.example.musicplace.main.dto;

public class FollowSaveDto {
    private String target_id;

    public FollowSaveDto(String target_id) {
        this.target_id = target_id;
    }

    public String getTarget_id() {
        return target_id;
    }
}
