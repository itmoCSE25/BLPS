package com.example.springmanageservice.model;

import org.springframework.jdbc.core.RowMapper;

public record DbStation(
        Long id,
        String stationName
) {

    public static RowMapper<DbStation> MAPPER = (rs, rowNum) -> new DbStation(
            rs.getLong("id"),
            rs.getString("name")
    );
}
