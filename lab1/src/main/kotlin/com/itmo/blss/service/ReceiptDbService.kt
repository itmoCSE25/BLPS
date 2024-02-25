package com.itmo.blss.service

import com.itmo.blss.model.db.Receipt

interface ReceiptDbService {

    fun saveReceiptInfo(receipt: Receipt): Boolean
}