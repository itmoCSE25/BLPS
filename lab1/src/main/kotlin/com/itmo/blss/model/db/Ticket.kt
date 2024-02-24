package com.itmo.blss.model.db

class Ticket(
    val ticketId: Long = -1,
    val userId: Long,
    val name: String,
    val surname: String,
    val routeId: Long,
    val trainId: Long,
    val vanId: Long,
    val seatId: Long
) {
}