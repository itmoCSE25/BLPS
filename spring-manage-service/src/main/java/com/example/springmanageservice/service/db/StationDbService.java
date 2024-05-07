package com.example.springmanageservice.service.db;

import java.util.List;

import com.example.springmanageservice.model.DbStation;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StationDbService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public StationDbService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<DbStation> getStations() {
        return namedParameterJdbcTemplate.query(
                "select * from stations",
                new MapSqlParameterSource(),
                DbStation.MAPPER
        );
    }
}
