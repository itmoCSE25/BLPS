package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Train
import com.itmo.blss.service.TrainsDbService
import com.itmo.blss.utils.TRAIN_MAPPER
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

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

    @Transactional(propagation = Propagation.MANDATORY)
    override fun createTrain(train: Train) {
        val sql = """
            insert into trains (train_num, route_id)
            values (:trainNum, :routeId)
        """.trimIndent()

        with(train) {
            namedParameterJdbcTemplate.update(
                sql,
                MapSqlParameterSource()
                    .addValue("trainNum", trainNum)
                    .addValue("routeId", routeId)
            )
        }
    }
}