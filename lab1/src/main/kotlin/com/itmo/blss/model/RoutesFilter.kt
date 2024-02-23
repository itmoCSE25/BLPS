package com.itmo.blss.model

import java.time.Instant

class RoutesFilter(
    val arrivalTime: Instant?,
    val arrivalStation: String?,
    val departureTime: Instant?,
    val departureStation: String?
) {
}