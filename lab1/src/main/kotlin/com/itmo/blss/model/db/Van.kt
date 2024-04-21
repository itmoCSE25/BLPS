package com.itmo.blss.model.db

import com.itmo.blss.model.enums.VanType

class Van(
    val vanId: Long = -1,
    val vanType: VanType,
    val trainId: Long,
    val vanNum: Long
)