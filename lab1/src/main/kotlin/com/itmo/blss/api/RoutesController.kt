package com.itmo.blss.api

import com.itmo.blss.api.response.RouteDto
import com.itmo.blss.model.RoutesFilter
import com.itmo.blss.model.db.Route
import com.itmo.blss.service.RoutesDbService
import com.itmo.blss.utils.ApiConstraints.Companion.ARRIVAL_STATION_KEY
import com.itmo.blss.utils.ApiConstraints.Companion.ARRIVAL_TIME_KEY
import com.itmo.blss.utils.ApiConstraints.Companion.DEPARTURE_STATION_KEY
import com.itmo.blss.utils.ApiConstraints.Companion.DEPARTURE_TIME_KEY
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/routes")
class RoutesController(
    private val routesDbService: RoutesDbService
) {

    @GetMapping
    fun getRoutesWithFilter(
        @RequestParam(value = ARRIVAL_TIME_KEY)
        arrivalTime: Instant?,
        @RequestParam(value = ARRIVAL_STATION_KEY)
        arrivalStation: String?,
        @RequestParam(value = DEPARTURE_TIME_KEY)
        departureTime: Instant?,
        @RequestParam(value = DEPARTURE_STATION_KEY)
        departureStation: String?
    ): List<RouteDto> {
        return routesDbService.getRoutesWithFilter(
            RoutesFilter(
                arrivalTime = arrivalTime,
                arrivalStation = arrivalStation,
                departureTime = departureTime,
                departureStation = departureStation
            )
        )
            .map { it.toRouteResponse() }
    }

    private fun Route.toRouteResponse() = RouteDto(
        this.routeId,
        this.arrivalTime.toString(),
        this.departureTime.toString(),
        this.arrivalStation,
        this.departureStation
    )
}