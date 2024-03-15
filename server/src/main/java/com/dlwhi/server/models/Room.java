package com.dlwhi.server.models;

public class Room {
    private Long id;
    private String name;
    private Long ownerId;

    public Room() {
    }

    public Room(Long id, String name, Long ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long owner) {
        this.ownerId = owner;
    }

    @Override
    public String toString() {
        return "Room [id=" + id + ", name=" + name + ", ownerId=" + ownerId + "]";
    }
}
