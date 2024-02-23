package com.itmo.blss.service

import com.itmo.blss.model.db.Train

interface TrainsDbService {

    fun getTrains(routeId: Int): List<Train>
}