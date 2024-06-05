package com.itmo.blss.service.impl;

import java.util.List;

import com.itmo.blss.model.db.Station;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

@Service
public class MasterDbService {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MasterDbService(
            NamedParameterJdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertIntoMaster(List<Station> stations) {
        String updateSql = """
            insert into stations (id, name)
            values (:id, :name)
            on conflict (id)
            do update set
            name = excluded.name
        """;
        jdbcTemplate.batchUpdate(updateSql, SqlParameterSourceUtils.createBatch(stations));
    }
}
