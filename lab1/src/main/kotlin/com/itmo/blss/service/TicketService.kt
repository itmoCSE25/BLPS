package com.itmo.blss.service

import com.itmo.blss.model.UserInfoDto
import com.itmo.blss.model.enums.TransactionStatus

interface TicketService {
    fun createTicket(userId: Long, userInfoDto: UserInfoDto): TransactionStatus
}