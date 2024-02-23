package com.itmo.blss.model.enums

enum class VanType(val code: Int, val type: String) {
    UNDEFINED(0, "Не определен"),
    PASSENGER(1, "Пассажирский"),
    FREIGHT(2, "Грузовой");

    companion object {
        infix fun from(code: Int): VanType? = VanType.values().firstOrNull { it.code == code }
    }
}