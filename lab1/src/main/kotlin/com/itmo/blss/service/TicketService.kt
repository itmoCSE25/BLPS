package com.itmo.blss.service

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.UserInfoDto
import com.itmo.blss.model.db.Ticket

interface TicketService {

    fun createTicket(userId: Long, userInfoDto: UserInfoDto): Ticket

    fun getTicketsInfoByFilter(ticketFilter: TicketFilter): List<TicketInfo>
}