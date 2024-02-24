package com.itmo.blss.api

import com.itmo.blss.api.response.TicketResponse
import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.UserInfoDto
import com.itmo.blss.service.TicketService
import com.itmo.blss.utils.ApiConstraints.Companion.TICKET_IDS_KEY
import com.itmo.blss.utils.ApiConstraints.Companion.USER_ID_KEY
import org.springframework.web.bind.annotation.GetMapping
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
    ): TicketResponse {
        return TicketResponse.new(ticketService.createTicket(userId, userInfoDto))
    }

    @GetMapping
    fun getTicketsByUserId(
        @RequestParam(value = USER_ID_KEY)
        userId: Long,
        @RequestParam(value = TICKET_IDS_KEY)
        ticketIds: List<Long>
    ): List<TicketResponse> {
        return ticketService.getTicketsByFilter(
            TicketFilter(userId = userId, ticketIds = ticketIds)
        )
            .map { TicketResponse.new(it) }
    }
}