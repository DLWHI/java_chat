package com.dlwhi.server.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dlwhi.server.models.User;
import com.dlwhi.server.models.UserRowMapper;

public class TemplateUserRepository implements UserRepository {
    private final String COLUMN_SELECT = 
        "id as user_id, " + 
        "username as user_name, " + 
        "password as user_passwd";

    private final String FIND_QUERY_ID =
        "select " + COLUMN_SELECT + " from users where id = :id;";
    private final String FIND_QUERY_USERNAME =
        "select " + COLUMN_SELECT + " from users where username = :user;";
    private final String INSERT_QUERY =
        "insert into users(username, password) " +
        "values(:user, :passwd);";
    private final String UPDATE_QUERY = 
        "update users set username = :user, password = :passwd where id = :id;";
    private final String DELETE_QUERY =
        "delete from users where id = :id;";
    private final String GET_ALL_QUERY =
        "select " + COLUMN_SELECT + " from users;";

    private final DataSource db;
    private final UserRowMapper mapper = 
        new UserRowMapper("user_id", "user_name", "user_passwd"); 

    public TemplateUserRepository(DataSource db) {
        this.db = db;
    }

    @Override
    public User findById(long id) throws DataAccessException {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        List<User> found = query.query(
            FIND_QUERY_ID,
            new MapSqlParameterSource("id", id),
            mapper
        );
        return (found.isEmpty()) ? null : found.get(0);
    }

    @Override
    public List<User> getAll() {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.query(
            GET_ALL_QUERY,
            mapper
        );
    }

    @Override
    public boolean save(User entity) {
        if (entity == null || entity.getUsername() == null || entity.getPassword() == null) {
            return false;
        }
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        try {
            return query.update(
                INSERT_QUERY,
                new MapSqlParameterSource("user", entity.getUsername())
                    .addValue("passwd", entity.getPassword())
            ) == 1;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public boolean update(User entity) {
        if (entity == null || entity.getUsername() == null || entity.getPassword() == null) {
            return false;
        }
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.update(
            UPDATE_QUERY, 
            new MapSqlParameterSource("id", entity.getId())
                .addValue("user", entity.getUsername())
                .addValue("passwd", entity.getPassword())
        ) == 1;
    }

    @Override
    public void delete(long id) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        query.update(DELETE_QUERY, new MapSqlParameterSource("id", id));
    }

    @Override
    public User findByUsername(String username) throws DataAccessException {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        List<User> found = query.query(
            FIND_QUERY_USERNAME,
            new MapSqlParameterSource("user", username),
            mapper
        );
        return (found.isEmpty()) ? null : found.get(0);
    }
}
