package com.itmo.blss.model.create

data class CreateRouteDto(
    val departureStationId: Long,
    val arrivalStationId: Long,
    val departureTime: String,
    val arrivalTime: String
)
