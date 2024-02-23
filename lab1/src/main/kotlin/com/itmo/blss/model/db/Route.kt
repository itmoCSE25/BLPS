package com.itmo.blss.model.db

import java.time.Instant

class Route(
    val arrivalTime: Instant,
    val departureTime: Instant,
    val arrivalStation: String,
    val departureStation: String
)