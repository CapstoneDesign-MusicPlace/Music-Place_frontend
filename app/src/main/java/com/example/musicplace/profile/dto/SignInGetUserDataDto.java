package com.example.musicplace.profile.dto;

public class SignInGetUserDataDto {

    private String name;

    private String email;

    private String nickname;

    private String profile_img_url;

    public SignInGetUserDataDto(String name, String email, String nickname, String profile_img_url) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.profile_img_url = profile_img_url;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }
}
