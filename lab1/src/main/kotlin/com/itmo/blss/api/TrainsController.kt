package com.itmo.blss.api

import com.itmo.blss.api.response.TrainDto
import com.itmo.blss.model.db.Train
import com.itmo.blss.service.TrainsDbService
import com.itmo.blss.utils.ApiConstraints.Companion.ROUTE_ID_KEY
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trains")
class TrainsController(
    private val trainsDbService: TrainsDbService
) {

    @GetMapping
    fun getTrains(
        @RequestParam(value = ROUTE_ID_KEY)
        routeId: Int
    ): List<TrainDto> {
        return trainsDbService.getTrains(routeId)
            .map { it.toTrainResponse() }
    }

    private fun Train.toTrainResponse() = TrainDto(
        this.trainId,
        this.trainNum
    )
}