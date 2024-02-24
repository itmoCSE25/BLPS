package com.itmo.blss.api.response

data class RouteResponse(
    val routeId: Long,
    val arrivalTime: String,
    val departureTime: String,
    val arrivalStation: String,
    val departureStation: String
) {
}