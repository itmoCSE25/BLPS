package com.itmo.blss.api

import com.itmo.blss.api.response.TrainDto
import com.itmo.blss.model.create.CreateTrainDto
import com.itmo.blss.model.db.Train
import com.itmo.blss.service.TrainsDbService
import com.itmo.blss.utils.ApiConstraints.Companion.ROUTE_ID_KEY
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trains")
class TrainsController(
    private val trainsDbService: TrainsDbService,
    private val transactionTemplate: TransactionTemplate
) {

    @GetMapping
    fun getTrains(
        @RequestParam(value = ROUTE_ID_KEY)
        routeId: Int
    ): ResponseEntity<List<TrainDto>> {
        return ResponseEntity.ok(
            trainsDbService.getTrains(routeId)
                .map { it.toTrainResponse() }
        )
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun createTrains(
        @RequestBody createTrainDto: CreateTrainDto
    ): ResponseEntity<Void> {
        transactionTemplate.execute {
            trainsDbService.createTrain(createTrainDto.toModel())
        }
        return ResponseEntity.ok().build()
    }

    private fun Train.toTrainResponse() = TrainDto(
        this.trainId,
        this.trainNum
    )

    private fun CreateTrainDto.toModel() = Train(
        trainNum = this.trainNum,
        routeId = this.routeId
    )
}