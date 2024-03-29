package com.itmo.blss.api.response

import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.db.Ticket

data class TicketDto(
    val userId: Long,
    val userName: String,
    val userSurname: String,
    val routeId: Long,
    val trainId: Long,
    val vanId: Long,
    val seatId: Long,
) {
    companion object {
        fun new(ticket: Ticket) = TicketDto(
            ticket.userId,
            ticket.name,
            ticket.surname,
            ticket.routeId,
            ticket.trainId,
            ticket.vanId,
            ticket.seatId
        )

        fun new(ticketInfo: TicketInfo) = TicketDto(
            ticketInfo.ticket.userId,
            ticketInfo.ticket.name,
            ticketInfo.ticket.surname,
            ticketInfo.ticket.routeId,
            ticketInfo.ticket.trainId,
            ticketInfo.ticket.vanId,
            ticketInfo.ticket.seatId
        )
    }
}