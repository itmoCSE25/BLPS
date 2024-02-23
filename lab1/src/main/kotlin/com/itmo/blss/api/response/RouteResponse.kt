package com.itmo.blss.api.response

data class RouteResponse(
    val arrivalTime: String,
    val departureTime: String,
    val arrivalStation: String,
    val departureStation: String
) {
}