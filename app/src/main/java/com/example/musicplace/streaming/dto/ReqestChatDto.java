package com.example.musicplace.streaming.dto;

public class ReqestChatDto {
    private String message;
    private String chatRoomId;
    private String vidioId;

    public ReqestChatDto(String message, String chatRoomId, String vidioId) {
        this.message = message;
        this.chatRoomId = chatRoomId;
        this.vidioId = vidioId;
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
