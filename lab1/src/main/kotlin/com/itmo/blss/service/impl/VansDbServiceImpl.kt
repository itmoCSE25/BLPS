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

    override fun getVan(vanId: Long): Van {
        val sql = """
            select * from vans
            where id = (:vanId)
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql, MapSqlParameterSource("vanId", vanId),
            VAN_MAPPER
        ).first()
    }

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

    override fun createVan(van: Van) {
        val sql = """
            insert into vans (train_id, van_type, van_num)
            values (:trainId, :vanType, :vanNum)
        """.trimIndent()

        with(van) {
            namedParameterJdbcTemplate.update(
                sql, MapSqlParameterSource()
                    .addValue("trainId", trainId)
                    .addValue("vanType", vanType.code)
                    .addValue("vanNum", vanNum)
            )
        }
    }
}