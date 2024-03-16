package com.dlwhi.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MessageRowMapper implements RowMapper<Message> {
    private final String ID_COLUMN_LABEL;
    private final String TEXT_COLUMN_LABEL;
    private final String ROOMID_COLUMN_LABEL;
    private final UserRowMapper AUTHOR_MAPPER;


    public MessageRowMapper(String idCol, String textCol, String roomCol, UserRowMapper authorMapper) {
        ID_COLUMN_LABEL = idCol;
        TEXT_COLUMN_LABEL = textCol;
        ROOMID_COLUMN_LABEL = roomCol;
        AUTHOR_MAPPER = authorMapper;
    }


    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        User author = AUTHOR_MAPPER.mapRow(rs, rowNum);
        long id = rs.getLong(ID_COLUMN_LABEL);
        String text = rs.getString(TEXT_COLUMN_LABEL);
        long roomId = rs.getLong(ROOMID_COLUMN_LABEL);
        return new Message(id, text, author, roomId);
    }
}
