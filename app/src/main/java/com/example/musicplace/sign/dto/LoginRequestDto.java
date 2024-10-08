package com.example.musicplace.sign.dto;

import com.google.gson.annotations.SerializedName;

public class LoginRequestDto {
    @SerializedName("member_id")
    private String id;
    @SerializedName("pw")
    private String password;

    public LoginRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }

}