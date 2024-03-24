package com.itmo.blss.api.response

import com.itmo.blss.model.enums.VanType

data class VanDto(
    val vanId: Long,
    val vanType: VanType,
    val trainId: Long
)
