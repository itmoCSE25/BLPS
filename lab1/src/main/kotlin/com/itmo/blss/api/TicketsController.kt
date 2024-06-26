package com.itmo.blss.api

import com.itmo.blss.api.response.ReceiptDto
import com.itmo.blss.api.response.TicketDto
import com.itmo.blss.api.response.TicketFullInfoDto
import com.itmo.blss.api.response.TicketResponse
import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.UserTicketInfo
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.service.TicketService
import com.itmo.blss.utils.ApiConstraints.Companion.TICKET_IDS_KEY
import com.itmo.blss.utils.ApiConstraints.Companion.USER_ID_KEY
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tickets")
class TicketsController(
    private val ticketService: TicketService,
): JavaDelegate {

    @PostMapping("/create")
    fun createTicketForUser(
        @RequestParam(value = USER_ID_KEY)
        userId: Long,
        @RequestBody
        userInfoDto: UserTicketInfo
    ): ResponseEntity<TicketResponse> {
        ticketService.getTicketsInfoByFilter(TicketFilter(userId)).firstOrNull()?.ticket?.let {
            if (compareTicketData(it, userInfoDto)) {
                return ResponseEntity.ok(
                    TicketResponse(TicketDto.new(it), "Ticket already exist")
                )
            }
        }
        return ResponseEntity.ok(
            TicketResponse(
                TicketDto.new(ticketService.createTicket(userId, userInfoDto)),
                "Ticket was created"
            )
        )
    }

    @GetMapping
    fun getTicketsByFilter(
        @RequestParam(value = USER_ID_KEY)
        userId: Long?,
        @RequestParam(value = TICKET_IDS_KEY)
        ticketIds: List<Long>?
    ): ResponseEntity<List<TicketFullInfoDto>> {
        val res = ticketService.getTicketsInfoByFilter(
            TicketFilter(userId = userId, ticketIds = ticketIds)
        )
            .map {
                TicketFullInfoDto(
                    TicketDto.new(it.ticket), ReceiptDto.new(it.receipt)
                )
            }
        return ResponseEntity.ok(res)
    }

    private fun compareTicketData(ticket: Ticket, userInfoDto: UserTicketInfo): Boolean {
        return ticket.routeId == userInfoDto.routeId &&
            ticket.trainId == userInfoDto.trainId &&
            ticket.vanId == userInfoDto.vanId &&
            ticket.seatId == userInfoDto.seatId
    }

    override fun execute(p0: DelegateExecution) {
        val userTicketInfo = UserTicketInfo(
            name = p0.getVariable("name") as String,
            surname = p0.getVariable("surname") as String,
            birthday = p0.getVariable("birthday") as String,
            routeId = p0.getVariable("route") as Long,
            trainId = p0.getVariable("train") as Long,
            vanId = p0.getVariable("van") as Long,
            seatId = p0.getVariable("seat") as Long
        )

        ticketService.createTicket(1, userTicketInfo)
        p0.setVariable("isOk", true)
    }
}
