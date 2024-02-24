package com.itmo.blss.service

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.model.enums.TransactionStatus

interface TicketDbService {

    fun saveTicketInfo(dbTicket: Ticket): Ticket

    fun saveTicketTransactionInfo(ticketId: Long, transactionId: Long, transactionStatus: TransactionStatus): Boolean

    fun getTicketsByFilter(ticketFilter: TicketFilter): List<Ticket>
}