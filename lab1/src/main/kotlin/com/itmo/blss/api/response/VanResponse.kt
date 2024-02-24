package com.itmo.blss.api.response

import com.itmo.blss.model.enums.VanType

data class VanResponse(
    val vanId: Long,
    val vanType: VanType,
    val trainId: Long
)
