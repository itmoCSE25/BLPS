package com.itmo.blss.api

import com.itmo.blss.api.response.StationDto
import com.itmo.blss.model.create.CreateStationDto
import com.itmo.blss.model.db.Station
import com.itmo.blss.service.StationDbService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/station")
class StationController(
    private val stationDbService: StationDbService,
) {

    @GetMapping
    fun getStations(): List<StationDto> {
        return stationDbService.getStations().map { it.toDto() }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun createStation(
        @RequestBody createStation: CreateStationDto
    ): ResponseEntity<Void> {
        stationDbService.createStation(Station(name = createStation.name))
        return ResponseEntity.ok().build()
    }

    private fun Station.toDto() = StationDto(
        this.id,
        this.name
    )
}
