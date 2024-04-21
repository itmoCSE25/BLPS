package com.itmo.blss.service

import com.itmo.blss.model.db.Van

interface VansDbService {

    fun getVan(vanId: Long): Van

    fun getVansByTrainId(trainId: Long): List<Van>

    fun createVan(van: Van)
}