package com.dlwhi.server.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dlwhi.server.models.Message;
import com.dlwhi.server.models.MessageRowMapper;

public class TemplateMessageRepository implements MessageRepository {
    private final String COLUMN_SELECT = 
        "messages.id as message_id, " + 
        "messages.text as message_text, " + 
        "messages.room as message_room_id, " +
        "users.id as author_id, " +
        "users.username as author_name ";

    private final String FIND_QUERY_ID =
        "select " + COLUMN_SELECT + " from messages " +
        "join users on users.id = messages.author " +
        "where id = :id;";
    private final String FIND_QUERY_ROOM =
        "select " + COLUMN_SELECT + " from messages " +
        "join users on users.id = messages.author " +
        "where room = :roomId;";
    private final String INSERT_QUERY =
        "insert into messages(text, author, room) " +
        "values(:text, :authorId, :roomId);";
    private final String UPDATE_QUERY = 
        "update messages set text = :text where id = :id;";
    private final String DELETE_QUERY =
        "delete from messages where id = :id;";
    private final String GET_ALL_QUERY =
        "select " + COLUMN_SELECT + " from messages " +
        "join users on users.id = messages.author;";

    private final DataSource db;
    private final MessageRowMapper mapper =
        new MessageRowMapper(
            "message_id",
            "message_text",
            "message_room_id",
            "author_id",
            "author_name"
        );

    public TemplateMessageRepository(DataSource db) {
        this.db = db;
    }

    @Override
    public Message findById(long id) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        List<Message> found = query.query(
            FIND_QUERY_ID,
            new MapSqlParameterSource("id", id),
            mapper
        );
        return (found.isEmpty()) ? null : found.get(0);
    }

    @Override
    public List<Message> getAll() {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.query(
            GET_ALL_QUERY,
            mapper
        );
    }

    @Override
    public boolean save(Message entity) {
        if (entity == null 
            || entity.getAuthor() == null 
            || entity.getRoomId() == null 
            || entity.getText() == null) {
            return false;
        }
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        try {
            return query.update(
                INSERT_QUERY,
                new MapSqlParameterSource("text", entity.getText())
                    .addValue("authorId", entity.getAuthor().getId())
                    .addValue("roomId", entity.getRoomId())
            ) == 1;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public boolean update(Message entity) {
        if (entity == null || entity.getText() == null) {
            return false;
        }
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.update(
            UPDATE_QUERY, 
            new MapSqlParameterSource("id", entity.getId())
                .addValue("text", entity.getText())
        ) == 1;
    }

    @Override
    public void delete(long id) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        query.update(DELETE_QUERY, new MapSqlParameterSource("id", id));
    }

    @Override
    public List<Message> getAllInRoom(long roomId) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.query(
            FIND_QUERY_ROOM,
            new MapSqlParameterSource("roomId", roomId),
            mapper
        );
    }
    
}
