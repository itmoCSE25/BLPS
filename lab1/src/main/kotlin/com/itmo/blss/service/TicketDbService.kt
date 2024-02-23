package com.itmo.blss.service

import com.itmo.blss.model.db.Ticket
import com.itmo.blss.model.enums.TransactionStatus

interface TicketDbService {
    fun saveTicketInfo(dbTicket: Ticket): Long

    fun saveTicketTransactionInfo(ticketId: Long, transactionId: Long, transactionStatus: TransactionStatus): Boolean
}