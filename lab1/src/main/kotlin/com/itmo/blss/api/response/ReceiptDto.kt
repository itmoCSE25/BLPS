package com.itmo.blss.api.response

import com.itmo.blss.model.db.Receipt
import com.itmo.blss.model.enums.TransactionStatus

data class ReceiptDto(
    val transactionId: Long,
    val transactionStatus: TransactionStatus
) {

    companion object{
        fun new(receipt: Receipt) = ReceiptDto(
            transactionId = receipt.transactionId,
            transactionStatus = receipt.transactionStatus
        )
    }
}