package com.itmo.blss.model

data class UserInfoDto(
    val name: String,
    val surname: String,
    val birthday: String,
    val routeId: Long,
    val trainId: Long,
    val vanId: Long,
    val seatId: Long
)