package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Van
import com.itmo.blss.service.VansDbService
import com.itmo.blss.utils.VAN_MAPPER
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class VansDbServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
): VansDbService {

    override fun getVansByTrainId(trainId: Long): List<Van> {
        val sql = """
            select * from vans
            where train_id = (:trainId)
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            MapSqlParameterSource("trainId", trainId),
            VAN_MAPPER
        )
    }
}