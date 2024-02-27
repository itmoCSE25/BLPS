package com.itmo.blss.api.response

data class TicketResponse(
    val ticketDto: TicketDto?,
    val message: String?
) {
}