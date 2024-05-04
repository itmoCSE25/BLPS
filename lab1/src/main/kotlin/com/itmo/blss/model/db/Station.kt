package com.itmo.blss.model.db

import com.fasterxml.jackson.annotation.JsonProperty

data class Station(
    @JsonProperty("id")
    val id: Long = 0,
    @JsonProperty("stationName")
    val name: String
) {
}
