package com.dlwhi.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.dlwhi.server.exceptions.MismatchedColumnsException;

public class User {
    private static final String tableName = "users";
    
    private Long id = null;
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
        created.id = queryResult.getLong(User.tableName + ".id");
        created.username = queryResult.getString(User.tableName + ".username");
        created.password = queryResult.getString(User.tableName + ".password");
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

    public SqlParameterSource getParamSource() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (id != null) {
            params.addValue(tableName + ".id", id);
        }
        params.addValue(tableName + ".un", username);
        params.addValue(tableName + ".passwd", password);
        return params;
    }

    public String getUsername() {
        return username;
    }

    public boolean passwdMatches(String passwd) {
        return passwd.equals(password);
    }
}
