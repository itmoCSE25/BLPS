package com.itmo.blss.service.impl

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.service.TicketDbService
import com.itmo.blss.utils.TICKET_INFO_MAPPER
import com.itmo.blss.utils.TICKET_MAPPER
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.lang.StringBuilder

@Service
class TicketDbServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : TicketDbService {

    override fun saveTicketInfo(dbTicket: Ticket): Ticket {
        val sql = """
            insert into tickets (user_id, name, surname, route_id, train_id, van_id, seat_id) 
            VALUES (:userId, :name, :surname, :routeId, :trainId, :vanId, :seatId)
            on conflict (route_id, train_id, van_id, seat_id) do nothing
            returning *
        """.trimIndent()

        return namedParameterJdbcTemplate.queryForObject(
            sql, MapSqlParameterSource()
                .addValue("userId", dbTicket.userId)
                .addValue("name", dbTicket.name)
                .addValue("surname", dbTicket.surname)
                .addValue("routeId", dbTicket.routeId)
                .addValue("trainId", dbTicket.trainId)
                .addValue("vanId", dbTicket.vanId)
                .addValue("seatId", dbTicket.seatId),
            TICKET_MAPPER
        )!!
    }

    override fun getTicketsInfoByFilter(ticketFilter: TicketFilter): List<TicketInfo> {
        //language=SQL
        val sql = """
            select
                t.id as id,
                t.user_id as user_id,
                t.name as name,
                t.surname as surname,
                t.route_id as route_id,
                t.train_id as train_id,
                t.van_id as van_id,
                t.seat_id as seat_id,
                r.transaction_status as transaction_status,
                r.transaction_id as transaction_id
            from tickets t
            left join receipt r on t.id = r.ticket_id
            where 1=1
        """.trimIndent()

        val sqlAndParams = buildWhereClause(sql, ticketFilter)

        return namedParameterJdbcTemplate.query(
            sqlAndParams.first,
            sqlAndParams.second,
            TICKET_INFO_MAPPER
        )
    }

    private fun buildWhereClause(sql: String, ticketFilter: TicketFilter): Pair<String, MapSqlParameterSource> {
        val resSql = StringBuilder(sql)
        val mapSqlParameterSource = MapSqlParameterSource()

        ticketFilter.ticketIds?.run {
            resSql.append("\nand t.id in (:ticketIds)")
            mapSqlParameterSource.addValue("ticketIds", this)
        }
        ticketFilter.userId?.run {
            resSql.append("\nand t.user_id = (:userId)")
            mapSqlParameterSource.addValue("userId", this)
        }

        return Pair(resSql.toString(), mapSqlParameterSource)
    }
}