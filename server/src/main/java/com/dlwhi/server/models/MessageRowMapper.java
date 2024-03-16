package com.dlwhi.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MessageRowMapper implements RowMapper<Message> {
    private final String ID_COLUMN_LABEL;
    private final String TEXT_COLUMN_LABEL;
    private final String ROOMID_COLUMN_LABEL;
    private final String AUTHORID_COLUMN_LABEL;
    private final String AUTHORNAME_COLUMN_LABEL;


    public MessageRowMapper(String idCol, String textCol, String roomCol, String authorIdCol, String authorNameCol) {
        ID_COLUMN_LABEL = idCol;
        TEXT_COLUMN_LABEL = textCol;
        ROOMID_COLUMN_LABEL = roomCol;
        AUTHORID_COLUMN_LABEL = authorIdCol;
        AUTHORNAME_COLUMN_LABEL = authorNameCol;
    }


    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        long author_id = rs.getLong(AUTHORID_COLUMN_LABEL);
        String author_name = rs.getString(AUTHORNAME_COLUMN_LABEL);
        long id = rs.getLong(ID_COLUMN_LABEL);
        String text = rs.getString(TEXT_COLUMN_LABEL);
        long roomId = rs.getLong(ROOMID_COLUMN_LABEL);
        return new Message(id, text, new User(author_id, author_name, null), roomId);
    }
}
