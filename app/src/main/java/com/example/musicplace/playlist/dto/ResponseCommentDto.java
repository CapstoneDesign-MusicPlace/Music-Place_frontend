package com.example.musicplace.playlist.dto;

public class ResponseCommentDto {
    private String memberId;

    private String nickName;

    private String userComment;

    private String profile_img_url;

    public ResponseCommentDto( String memberId, String nickName, String userComment, String profile_img_url) {

        this.memberId = memberId;
        this.nickName = nickName;
        this.userComment = userComment;
        this.profile_img_url = profile_img_url;
    }


    public String getMemberId() {
        return memberId;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserComment() {
        return userComment;
    }
}
