package com.example.musicplace.playlist.dto;

public class CommentSaveDto {
    private String nickName;

    private String comment;

    public CommentSaveDto(String nickName, String comment) {
        this.nickName = nickName;
        this.comment = comment;
    }

    public String getNickName() {
        return nickName;
    }

    public String getComment() {
        return comment;
    }
}
