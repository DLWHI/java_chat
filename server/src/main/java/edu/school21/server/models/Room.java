package edu.school21.server.models;

import java.util.List;

public class Room {
    private long id;
    private String name;
    private User owner;
    private List<Message> messages;
}
