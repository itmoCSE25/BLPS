package com.itmo.blss.model.db

import com.itmo.blss.model.enums.TransactionStatus

class Receipt(
    val id: Long = -1,
    val userId: Long,
    val ticketId: Long,
    val transactionId: Long,
    val transactionStatus: TransactionStatus
) {
}