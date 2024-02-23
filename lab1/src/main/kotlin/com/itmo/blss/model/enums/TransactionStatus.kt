package com.itmo.blss.model.enums

enum class TransactionStatus(val code: Int, val status: String) {
    UNDEFINED(0, "не определен"),
    NOT_PERMITTED(1, "не выполнялась"),
    ERROR(2, "Выполнена с ошибкой"),
    DONE(3, "Выполнена успешно")
}