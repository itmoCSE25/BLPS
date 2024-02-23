package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Ticket
import com.itmo.blss.model.enums.TransactionStatus
import com.itmo.blss.service.TicketDbService
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class TicketDbServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : TicketDbService {

    override fun saveTicketInfo(dbTicket: Ticket): Long {
        val sql = """
            insert into tickets (user_id, name, surname, route_id, train_id, van_id, seat_id) 
            VALUES (:userId, :name, :surname, :routeId, :trainId, :vanId, :seatId)
            on conflict (route_id, train_id, van_id, seat_id) do nothing
            returning id
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql, MapSqlParameterSource()
                .addValue("userId", dbTicket.userId)
                .addValue("name", dbTicket.name)
                .addValue("surname", dbTicket.surname)
                .addValue("route_id", dbTicket.routeId)
                .addValue("train_id", dbTicket.trainId)
                .addValue("van_id", dbTicket.vanId)
                .addValue("seat_id", dbTicket.seatId),
            SingleColumnRowMapper.newInstance(Long::class.java)
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
}