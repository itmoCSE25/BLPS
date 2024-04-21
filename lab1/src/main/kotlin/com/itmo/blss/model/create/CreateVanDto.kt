package com.itmo.blss.model.create

import com.itmo.blss.model.enums.VanType

data class CreateVanDto(
    val trainId: Long,
    val vanType: VanType,
    val vanNum: Long
)