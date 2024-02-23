package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Seat
import com.itmo.blss.service.SeatsDbService
import com.itmo.blss.utils.SEAT_MAPPER
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class SeatsDbServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
):SeatsDbService {

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
}