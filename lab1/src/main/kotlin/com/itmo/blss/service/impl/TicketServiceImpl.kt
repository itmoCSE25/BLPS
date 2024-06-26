package com.itmo.blss.service.impl

import com.itmo.blss.model.TicketFilter
import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.UserTicketInfo
import com.itmo.blss.model.db.Receipt
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.model.enums.TransactionStatus
import com.itmo.blss.service.BillingService
import com.itmo.blss.service.ReceiptDbService
import com.itmo.blss.service.RoutesDbService
import com.itmo.blss.service.SeatsDbService
import com.itmo.blss.service.TicketDbService
import com.itmo.blss.service.TicketService
import com.itmo.blss.service.TrainsDbService
import com.itmo.blss.service.VansDbService
import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration
import java.util.concurrent.ExecutorService

@Service
class TicketServiceImpl(
    private val ticketDbService: TicketDbService,
    private val receiptDbService: ReceiptDbService,
    private val transactionTemplate: TransactionTemplate,
    private val billingClient: BillingService,
    private val seatsDbService: SeatsDbService,
    private val vansDbService: VansDbService,
    private val trainsDbService: TrainsDbService,
    private val routesDbService: RoutesDbService,
    private val billingConsumer: KafkaConsumer<String, String>,
    private val executorService: ExecutorService
) : TicketService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun postConstruct() {
        // executorService.execute(this::initKafkaConsumer)
    }

    @Transactional
    override fun createTicket(userId: Long, userInfoDto: UserTicketInfo): Ticket {
        checkUserInfoDto(userInfoDto)
        return transactionTemplate.execute {
            val ticket = ticketDbService.saveTicketInfo(userInfoDto.toDbTicket(userId))
            // billingClient.sendBillingInfo(
            //     userId.toInt(), calculatePrice(userInfoDto).toDouble()
            // )
            receiptDbService.saveReceiptInfo(
                Receipt(
                    userId = userId,
                    ticketId = ticket.ticketId,
                    transactionId = -1,
                    transactionStatus = TransactionStatus.UNDEFINED
                )
            )
            ticket
        }!!
    }

    @Transactional
    override fun getTicketsInfoByFilter(ticketFilter: TicketFilter): List<TicketInfo> {
        return ticketDbService.getTicketsInfoByFilter(ticketFilter)
    }

    override fun addBillingInfo(userId: Long, transactionStatus: TransactionStatus) {
        receiptDbService.updateTransactionInfo(userId, transactionStatus)
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
            if (!trainsDbService.getTrains(routeId.toInt()).map { it.trainId }.contains(trainId)) {
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

    private fun calculatePrice(userInfoDto: UserTicketInfo): Long {
        return userInfoDto.seatId * userInfoDto.vanId * userInfoDto.routeId
    }

    private fun initKafkaConsumer() {
        logger.info("Start listening messages from billing service")
        billingConsumer.subscribe(listOf("billing-transactions-result"))
        while (true) {
            val records: ConsumerRecords<String, String> = billingConsumer.poll(Duration.ofMillis(100))
            for (record in records) {
                logger.info("Key: " + record.key() + ", Value: " + record.value())
                logger.info("Partition: " + record.partition() + ", Offset:" + record.offset())
                val values = record.value().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val userId = values[0].toLong()
                addBillingInfo(userId, TransactionStatus.DONE)
            }
        }
    }
}
