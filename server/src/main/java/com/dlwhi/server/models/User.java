package com.dlwhi.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dlwhi.server.exceptions.MismatchedColumnsException;

public class User {
    private long id;
    private String username;
    private String password;

    private User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static User fromResultSet(ResultSet queryResult) 
            throws SQLException {
        User created = new User();
        created.id = queryResult.getLong("id");
        created.username = queryResult.getString("username");
        created.password = queryResult.getString("password");
        return created;
    }

    public static User fromResultSet(ResultSet queryResult, String... columnNames) 
            throws SQLException {
        User created = new User();
        if (columnNames.length != 3) {
            throw new MismatchedColumnsException(
                String.format(
                    "Invalid column count, got %d, expected 3",
                    columnNames.length
                )
            );
        }
        created.id = queryResult.getLong(columnNames[0]);
        created.username = queryResult.getString(columnNames[1]);
        created.password = queryResult.getString(columnNames[2]);
        return created;
    }

    public long getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
