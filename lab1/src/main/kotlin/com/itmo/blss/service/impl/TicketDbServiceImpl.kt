package com.itmo.blss.service.impl

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.model.enums.TransactionStatus
import com.itmo.blss.service.TicketDbService
import com.itmo.blss.utils.TICKET_MAPPER
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.lang.RuntimeException
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

        return namedParameterJdbcTemplate.query(
            sql, MapSqlParameterSource()
                .addValue("userId", dbTicket.userId)
                .addValue("name", dbTicket.name)
                .addValue("surname", dbTicket.surname)
                .addValue("routeId", dbTicket.routeId)
                .addValue("trainId", dbTicket.trainId)
                .addValue("vanId", dbTicket.vanId)
                .addValue("seatId", dbTicket.seatId),
            TICKET_MAPPER
        ).firstOrNull() ?: throw RuntimeException()
    }

    override fun saveTicketTransactionInfo(ticketId: Long, transactionId: Long, transactionStatus: TransactionStatus): Boolean {
        val sql = """
            update tickets set
                transaction_status = (:transactionStatus),
                transaction_id = (:transactionId)
            where id = (:ticketId)
        """.trimIndent()

        return namedParameterJdbcTemplate.update(
            sql,
            MapSqlParameterSource()
                .addValue("transactionStatus", transactionStatus.code)
                .addValue("transactionId", transactionId)
                .addValue("ticketId", ticketId)
        ) > 0
    }

    override fun getTicketsByFilter(ticketFilter: TicketFilter): List<Ticket> {
        val sql = """
            select 
                t.user_id,
                t.name,
                t.surname,
                t.route_id,
                t.train_id,
                t.van_id,
                t.seat_id,
                t.transaction_status,
                t.transaction_id
            from tickets t
            where 1=1
        """.trimIndent()

        val sqlAndParams = buildWhereClause(sql, ticketFilter)

        return namedParameterJdbcTemplate.query(
            sqlAndParams.first,
            sqlAndParams.second,
            TICKET_MAPPER
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