package com.example.musicplace.profile.dto;

public class SignInUpdateDto {
    private String name;

    private String profile_img_url;

    private String email;

    private String nickname;

    public SignInUpdateDto(String name, String profile_img_url, String email, String nickname) {
        this.name = name;
        this.profile_img_url = profile_img_url;
        this.email = email;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
