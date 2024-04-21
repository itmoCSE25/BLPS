package com.itmo.blss.service.impl

import com.itmo.blss.model.create.CreateRouteDto
import com.itmo.blss.model.db.Route
import com.itmo.blss.service.RoutesDbService
import com.itmo.blss.service.RoutesService
import com.itmo.blss.service.StationDbService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class RoutesServiceImpl(
    private val stationDbService: StationDbService,
    private val routesDbService: RoutesDbService
) : RoutesService {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun createRoute(createRouteDto: CreateRouteDto) {
        validateCreateDto(createRouteDto)
        try {
            routesDbService.crateRoute(
                Route(
                    arrivalStationId = createRouteDto.arrivalStationId,
                    departureStationId = createRouteDto.departureStationId,
                    arrivalTime = Instant.parse(createRouteDto.arrivalTime),
                    departureTime = Instant.parse(createRouteDto.departureTime)
                )
            )
        } catch (e: Exception) {
            throw IllegalArgumentException("Duplicated unique value in sql")
        }
    }

    private fun validateCreateDto(createRouteDto: CreateRouteDto) {
        with(createRouteDto) {
            if (!stationDbService.isStationsExist(listOf(arrivalStationId, departureStationId))) {
                throw IllegalArgumentException("Stations with id: $arrivalStationId and $departureStationId isn't exist")
            }
            if (!Instant.parse(arrivalTime).isAfter(Instant.parse(departureTime))) {
                throw IllegalArgumentException("Deparute time is before arrival time")
            }
        }
    }
}