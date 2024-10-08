package com.example.musicplace.playlist.dto;

public class ResponseCommentDto {

    private Long comment_id;

    private String nickName;

    private String userComment;

    public ResponseCommentDto(Long comment_id, String nickName, String userComment) {
        this.comment_id = comment_id;
        this.nickName = nickName;
        this.userComment = userComment;
    }

    public Long getComment_id() {
        return comment_id;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserComment() {
        return userComment;
    }
}
