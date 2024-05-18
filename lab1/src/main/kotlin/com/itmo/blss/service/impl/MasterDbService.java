package com.itmo.blss.service.impl;

import java.util.List;

import javax.sql.DataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.itmo.blss.model.db.Station;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class MasterDbService {

    private final AtomikosDataSourceBean dataSourceBean2;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MasterDbService(AtomikosDataSourceBean dataSourceBean2) {
        this.dataSourceBean2 = dataSourceBean2;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSourceBean2);
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
