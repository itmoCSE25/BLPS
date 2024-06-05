package com.itmo.blss.service.impl

import com.itmo.blss.model.RoutesFilter
import com.itmo.blss.model.db.Route
import com.itmo.blss.service.RoutesDbService
import com.itmo.blss.utils.ROUTE_MAPPER
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.lang.StringBuilder
import java.sql.Timestamp

@Service
class RoutesDbService(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : RoutesDbService {

    override fun getRoutesWithFilter(routesFilter: RoutesFilter): List<Route> {
        //language=SQL
        val sql = """
            select
                r.id,
                r.arrival_time,
                r.departure_time,
                arrival.name as arrival_station,
                departure.name as departure_station
            from routes r
            join stations arrival on r.arrival_station_id = arrival.id
            join stations departure on r.departure_station_id = departure.id
            where 1=1
        """.trimIndent()
        val sqlAndParams = buildWhereClause(sql, routesFilter)
        return namedParameterJdbcTemplate.query(
            sqlAndParams.first,
            sqlAndParams.second,
            ROUTE_MAPPER
        )
    }

    override fun isRouteExist(routeId: Long): Boolean {
        val sql = """
            select id from routes
            where id = (:routeId)
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            MapSqlParameterSource("routeId", routeId),
            SingleColumnRowMapper.newInstance(Long::class.java)
        ).isNotEmpty()
    }

    @Transactional(propagation = Propagation.MANDATORY)
    override fun crateRoute(route: Route) {
        val sql = """
            insert into routes (departure_station_id, arrival_station_id, departure_time, arrival_time)
            values (:departureStation, :arrivalStation, :departureTime, :arrivalTime)
        """.trimIndent()

        with(route) {
            namedParameterJdbcTemplate.update(
                sql,
                MapSqlParameterSource()
                    .addValue("departureStation", departureStationId!!)
                    .addValue("arrivalStation", arrivalStationId!!)
                    .addValue("departureTime", Timestamp.from(departureTime))
                    .addValue("arrivalTime", Timestamp.from(arrivalTime))
            )
        }
    }

    private fun buildWhereClause(sql: String, routesFilter: RoutesFilter): Pair<String, MapSqlParameterSource> {
        val mapSqlParameterSource = MapSqlParameterSource()
        val resSql = StringBuilder(sql)
        routesFilter.arrivalTime?.run {
            resSql.append("\nand r.arrival_time < :arrivalTime\n")
            mapSqlParameterSource.addValue("arrivalTime", this)
        }
        routesFilter.departureTime?.run {
            resSql.append("\nand r.departure_time < :departureTime\n")
            mapSqlParameterSource.addValue("departureTime", routesFilter.departureTime)
        }
        routesFilter.arrivalStation?.run {
            resSql.append("\nand arrival.name = :arrivalStation\n")
            mapSqlParameterSource.addValue("arrivalStation", routesFilter.arrivalStation)
        }
        routesFilter.departureStation?.run {
            resSql.append("\nand departure.name = :departureStation\n")
            mapSqlParameterSource.addValue("departureStation", routesFilter.departureStation)
        }
        return Pair(resSql.toString(), mapSqlParameterSource)
    }
}
