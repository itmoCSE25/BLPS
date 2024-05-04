package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Station
import com.itmo.blss.service.StationDbService
import com.itmo.blss.utils.STATION_MAPPER
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class StationDbServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
): StationDbService {

    override fun getStations(): List<Station> {
        val sql = """
            select * from stations
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            MapSqlParameterSource(),
            STATION_MAPPER
        )
    }

    override fun getStationByName(name: String): Station {
        val sql = """
            select * from stations
            where name = :name
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            MapSqlParameterSource("name", name),
            STATION_MAPPER
        ).first()
    }

    override fun isStationsExist(ids: List<Long>): Boolean {
        val sql = """
            select id from stations
            where id in (:ids)
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            MapSqlParameterSource("ids", ids),
            SingleColumnRowMapper.newInstance(Long::class.java)
        ).size == ids.size
    }

    @Transactional(propagation = Propagation.MANDATORY)
    override fun createStation(station: Station) {
        val sql = """
            insert into stations (name) 
            values (:name)
        """.trimIndent()
        try {
            namedParameterJdbcTemplate.update(sql, MapSqlParameterSource("name", station.name))
        } catch (e: Exception) {
            throw IllegalArgumentException("Bad sql request. Duplicated unique value")
        }
    }

    @Transactional
    override fun upsertStations(stations: List<Station>) {
        val updateSql = """
            insert into stations (id, name)
            values (:id, :name)
            on conflict (id)
            do update set
            name = excluded.name
        """.trimIndent()
        namedParameterJdbcTemplate. batchUpdate(updateSql, SqlParameterSourceUtils.createBatch(stations))

        val deleteSql = """
            delete from stations
            where id not in (:ids);
        """.trimIndent()
        namedParameterJdbcTemplate.update(deleteSql, MapSqlParameterSource("ids", stations.map { it.id }))
    }
}
