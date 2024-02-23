package com.itmo.blss.service

import com.itmo.blss.model.db.Van

interface VansDbService {

    fun getVansByTrainId(trainId: Long): List<Van>
}