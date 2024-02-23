package com.itmo.blss.service.impl

import com.itmo.blss.model.enums.TransactionStatus
import com.itmo.blss.service.BillingService
import org.springframework.stereotype.Service

@Service
class BillingServiceMock : BillingService {
    override fun getInformationByTransaction(): Pair<Long, TransactionStatus> {
        return Pair(100, TransactionStatus.DONE)
    }
}