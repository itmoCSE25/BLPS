package com.itmo.blss.api

import com.itmo.blss.model.UserInfoDto
import com.itmo.blss.model.enums.TransactionStatus
import com.itmo.blss.service.TicketService
import com.itmo.blss.utils.ApiConstraints.Companion.USER_ID_KEY
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tickets")
class TicketsController(
    private val ticketService: TicketService
) {

    @PostMapping("/create")
    fun createTicketForUser(
        @RequestParam(value = USER_ID_KEY)
        userId: Long,
        @RequestBody
        userInfoDto: UserInfoDto
    ): TransactionStatus {
        return ticketService.createTicket(userId, userInfoDto)
    }

    fun getTicketsByUserId() {

    }
}