package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Train
import com.itmo.blss.service.TrainsDbService
import com.itmo.blss.utils.TRAIN_MAPPER
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class TrainsDbServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
): TrainsDbService {
    override fun getTrains(routeId: Int): List<Train> {
        val sql = """
            select * from trains
            where route_id = (:routeId)
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            MapSqlParameterSource("routeId", routeId),
            TRAIN_MAPPER
        )
    }
}