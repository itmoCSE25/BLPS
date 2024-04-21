package com.itmo.blss.model.enums

enum class VanType(val code: Int, val type: String, val maxSeatCount: Long) {
    UNDEFINED(0, "Не определен", 0),
    PASSENGER(1, "Пассажирский", 100),
    FREIGHT(2, "Грузовой", 2);

    companion object {
        infix fun from(code: Int): VanType? = VanType.values().firstOrNull { it.code == code }
    }
}