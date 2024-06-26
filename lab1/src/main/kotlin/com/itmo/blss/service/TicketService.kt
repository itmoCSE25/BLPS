package com.itmo.blss.service

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.UserTicketInfo
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.model.enums.TransactionStatus

interface TicketService {

    fun createTicket(userId: Long, userInfoDto: UserTicketInfo): Ticket

    fun getTicketsInfoByFilter(ticketFilter: TicketFilter): List<TicketInfo>

    fun addBillingInfo(userId: Long, transactionStatus: TransactionStatus)
}
