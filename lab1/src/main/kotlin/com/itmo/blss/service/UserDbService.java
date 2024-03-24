package com.itmo.blss.service;

import com.itmo.blss.model.jaas.Role;
import com.itmo.blss.model.jaas.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserDbService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDbService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(User user) {
        String sql = """
                insert into users (username, password, role)
                VALUES (:username, :password, :role)
                """;

        namedParameterJdbcTemplate.update(
                sql, new MapSqlParameterSource()
                        .addValue("username", user.getUsername())
                        .addValue("password", user.getPassword())
                        .addValue("role", user.getRole().toString())
        );
    }

    public User getByUsername(String username) {
        String sql = """
                select * from users
                where username = :username
                """;

        return namedParameterJdbcTemplate.query(
                sql,
                new MapSqlParameterSource("username", username),
                userRowMapper
        ).stream().findFirst().orElseThrow(() ->
                new IllegalArgumentException("User with name: %s not found".formatted(username))
        );
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User()
            .setId(rs.getLong("id"))
            .setUsername(rs.getString("username"))
            .setPassword(rs.getString("password"))
            .setRole(Role.valueOf(rs.getString("role")));
}
