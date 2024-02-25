package com.itmo.blss.service.impl

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.UserInfoDto
import com.itmo.blss.model.db.Receipt
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.service.BillingService
import com.itmo.blss.service.ReceiptDbService
import com.itmo.blss.service.TicketDbService
import com.itmo.blss.service.TicketService
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.lang.RuntimeException

@Service
class TicketServiceImpl(
    private val ticketDbService: TicketDbService,
    private val receiptDbService: ReceiptDbService,
    private val transactionTemplate: TransactionTemplate,

    private val billingService: BillingService
) : TicketService {

    override fun createTicket(userId: Long, userInfoDto: UserInfoDto): Ticket {
        return transactionTemplate.execute {
            val ticket = ticketDbService.saveTicketInfo(userInfoDto.toDbTicket(userId))
            val transaction = billingService.getInformationByTransaction()
            receiptDbService.saveReceiptInfo(
                Receipt(
                    userId = userId,
                    ticketId = ticket.ticketId,
                    transactionId = transaction.first,
                    transactionStatus = transaction.second
                )
            )
            ticket
        } ?: throw RuntimeException()
    }

    override fun getTicketsInfoByFilter(ticketFilter: TicketFilter): List<TicketInfo> {
        return ticketDbService.getTicketsInfoByFilter(ticketFilter)
    }

    private fun UserInfoDto.toDbTicket(userId: Long) = Ticket(
        userId = userId,
        name = this.name,
        surname = this.surname,
        routeId = this.routeId,
        trainId = this.trainId,
        vanId = this.vanId,
        seatId = this.seatId
    )
}