package com.itmo.blss.model.db

import java.time.Instant

class Route(
    val routeId: Long = -1,
    val arrivalTime: Instant,
    val departureTime: Instant,
    val arrivalStation: String? = null,
    val departureStation: String? = null,
    val departureStationId: Long? = null,
    val arrivalStationId: Long? = null,

)