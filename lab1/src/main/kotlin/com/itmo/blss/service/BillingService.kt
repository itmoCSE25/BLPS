package com.itmo.blss.service

import com.itmo.blss.model.enums.TransactionStatus

interface BillingService {

    fun getInformationByTransaction(): Pair<Long, TransactionStatus>
}