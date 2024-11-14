package com.example.musicplace.streaming.dto;

public class ResponseChatDto {
    private String message;
    private String chatRoomId;
    private String vidioId;
    private String userNickname;
    private int viewType;

    public ResponseChatDto(String message, String chatRoomId, String vidioId, String userNickname, int viewType) {
        this.message = message;
        this.chatRoomId = chatRoomId;
        this.vidioId = vidioId;
        this.userNickname = userNickname;
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public String getUserNickname() {
        return userNickname;
    }
    public String getMessage() {
        return message;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getVidioId() {
        return vidioId;
    }
}
