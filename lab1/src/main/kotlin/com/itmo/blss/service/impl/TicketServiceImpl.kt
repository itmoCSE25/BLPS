package com.itmo.blss.service.impl

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.UserTicketInfo
import com.itmo.blss.model.db.Receipt
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.service.BillingService
import com.itmo.blss.service.ReceiptDbService
import com.itmo.blss.service.RoutesDbService
import com.itmo.blss.service.SeatsDbService
import com.itmo.blss.service.TicketDbService
import com.itmo.blss.service.TicketService
import com.itmo.blss.service.TrainsDbService
import com.itmo.blss.service.VansDbService
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import kotlin.RuntimeException

@Service
class TicketServiceImpl(
    private val ticketDbService: TicketDbService,
    private val receiptDbService: ReceiptDbService,
    private val transactionTemplate: TransactionTemplate,
    private val billingService: BillingService,
    private val seatsDbService: SeatsDbService,
    private val vansDbService: VansDbService,
    private val trainsDbService: TrainsDbService,
    private val routesDbService: RoutesDbService
) : TicketService {

    override fun createTicket(userId: Long, userInfoDto: UserTicketInfo): Ticket {
        checkUserInfoDto(userInfoDto)
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
        }!!
    }

    override fun getTicketsInfoByFilter(ticketFilter: TicketFilter): List<TicketInfo> {
        return ticketDbService.getTicketsInfoByFilter(ticketFilter)
    }

    private fun UserTicketInfo.toDbTicket(userId: Long) = Ticket(
        userId = userId,
        name = this.name,
        surname = this.surname,
        routeId = this.routeId,
        trainId = this.trainId,
        vanId = this.vanId,
        seatId = this.seatId
    )

    private fun checkUserInfoDto(userInfoDto: UserTicketInfo) {
        with(userInfoDto) {
            if (!routesDbService.isRouteExist(routeId)) {
                throw RuntimeException("Route with id $routeId is not exist")
            }
            if (!trainsDbService.getTrains(routeId.toInt()).map {it.trainId}.contains(trainId)) {
                throw RuntimeException("Train with id $trainId is not presented in route with id $routeId")
            }
            if (!vansDbService.getVansByTrainId(trainId).map { it.vanId }.contains(vanId)) {
                throw RuntimeException("Van with id $vanId is not presented in train with id $trainId")
            }
            if (!seatsDbService.getSeatsByVanId(vanId).map { it.seatId }.contains(seatId)) {
                throw RuntimeException("Seat with id $seatId is not presented in van with id $vanId")
            }
        }
    }
}