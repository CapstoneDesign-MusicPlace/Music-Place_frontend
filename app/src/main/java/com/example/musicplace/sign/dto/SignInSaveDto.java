package com.example.musicplace.sign.dto;


import com.google.gson.annotations.SerializedName;

public class SignInSaveDto {
    @SerializedName("member_id")
    private String member_id;
    @SerializedName("pw")
    private String pw;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("gender")
    private Gender gender;


    public SignInSaveDto(String member_id, String pw, String name, String email, String nickname, Gender gender){
        this.member_id = member_id;
        this.pw = pw;
        this.gender = gender;
        this.email = email;
        this.nickname = nickname;
        this.name = name;
    }
}