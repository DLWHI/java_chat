package com.dlwhi.server.models;

public class Message {
    private Long id;
    private String text;
    private User author;
    private Long roomId;

    public Message() {
    }

    public Message(Long id, String text, User author, Long roomId) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(long room) {
        this.roomId = room;
    }
}
