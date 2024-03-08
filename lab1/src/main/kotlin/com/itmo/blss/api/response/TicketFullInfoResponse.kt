package com.itmo.blss.api.response

data class TicketFullInfoResponse(
    val ticketDto: TicketDto,
    val receiptDto: ReceiptDto
) {
}