package com.itmo.blss.api.response

import com.itmo.blss.model.db.Ticket

data class TicketResponse(
    val userId: Long,
    val userName: String,
    val userSurname: String,
    val routeId: Long,
    val trainId: Long,
    val vanId: Long,
    val seatId: Long
) {
    companion object {
        fun new(ticket: Ticket) = TicketResponse(
            ticket.userId,
            ticket.name,
            ticket.surname,
            ticket.routeId,
            ticket.trainId,
            ticket.vanId,
            ticket.seatId
        )
    }
}