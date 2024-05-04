package com.itmo.blss.service

interface BillingService {

    fun sendBillingInfo(userId: Int, amount: Double)

    fun getBillingInfo(): Pair<Int, Double>
}
