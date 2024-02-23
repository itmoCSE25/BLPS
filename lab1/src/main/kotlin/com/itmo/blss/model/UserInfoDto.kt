package com.itmo.blss.model

data class UserInfoDto(
    val name: String,
    val surname: String,
    val birthday: String,
    val trainId: Long,
    val routeId: Long,
    val vanId: Long,
    val seatId: Long
)