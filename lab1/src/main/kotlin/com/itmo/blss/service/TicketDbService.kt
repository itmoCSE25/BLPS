package com.itmo.blss.service

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.db.Ticket

interface TicketDbService {

    fun saveTicketInfo(dbTicket: Ticket): Ticket

    fun getTicketsInfoByFilter(ticketFilter: TicketFilter): List<TicketInfo>
}