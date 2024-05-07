package com.itmo.blss.service

import com.itmo.blss.model.db.Receipt
import com.itmo.blss.model.enums.TransactionStatus

interface ReceiptDbService {

    fun saveReceiptInfo(receipt: Receipt): Boolean

    fun updateTransactionInfo(userId: Long, transactionStatus: TransactionStatus)
}
