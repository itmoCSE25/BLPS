package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Seat
import com.itmo.blss.service.SeatsDbService
import com.itmo.blss.utils.SEAT_MAPPER
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class SeatsDbServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : SeatsDbService {

    override fun getSeatsByVanId(vanId: Long): List<Seat> {
        val sql = """
            select * from seats
            where van_id = (:vanId)
        """

        return namedParameterJdbcTemplate.query(
            sql,
            MapSqlParameterSource("vanId", vanId),
            SEAT_MAPPER
        )
    }

    @Transactional(propagation = Propagation.MANDATORY)
    override fun addSeats(seats: List<Seat>) {
        val sql = """
            insert into seats (van_id)
            values (:vanId)
        """.trimIndent()

        namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(seats))
    }

    override fun getSeatsCount(vanId: Long): Long {
        val sql = """
            select count(*) from seats
             where van_id = :vanId
        """.trimIndent()

        return namedParameterJdbcTemplate.queryForObject(
            sql,
            MapSqlParameterSource("vanId", vanId),
            Long::class.java
        )!!
    }
}