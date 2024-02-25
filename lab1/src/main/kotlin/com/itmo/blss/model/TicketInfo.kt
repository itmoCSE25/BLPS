package com.itmo.blss.model

import com.itmo.blss.model.db.Receipt
import com.itmo.blss.model.db.Ticket

class TicketInfo(
    val ticket: Ticket,
    val receipt: Receipt
) {
}