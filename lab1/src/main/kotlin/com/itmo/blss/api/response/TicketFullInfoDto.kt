package com.itmo.blss.api.response

data class TicketFullInfoDto(
    val ticketDto: TicketDto,
    val receiptDto: ReceiptDto
) {
}