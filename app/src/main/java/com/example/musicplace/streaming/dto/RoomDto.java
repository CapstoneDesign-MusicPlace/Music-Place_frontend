package com.example.musicplace.streaming.dto;

public class RoomDto {
    private String chatRoomId;
    private String roomTitle;
    private String roomComment;
    private String username;

    public RoomDto(String chatRoomId, String roomTitle, String roomComment, String username) {
        this.chatRoomId = chatRoomId;
        this.roomTitle = roomTitle;
        this.roomComment = roomComment;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }
    public String getRoomTitle() {
        return roomTitle;
    }

    public String getRoomComment() {
        return roomComment;
    }
}
