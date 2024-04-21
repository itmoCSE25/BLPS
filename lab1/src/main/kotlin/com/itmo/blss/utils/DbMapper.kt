package com.itmo.blss.utils

import com.itmo.blss.model.TicketInfo
import com.itmo.blss.model.db.Receipt
import com.itmo.blss.model.db.Route
import com.itmo.blss.model.db.Seat
import com.itmo.blss.model.db.Station
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.model.db.Train
import com.itmo.blss.model.db.Van
import com.itmo.blss.model.enums.TransactionStatus
import com.itmo.blss.model.enums.VanType
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

val ROUTE_MAPPER = RowMapper { rs, _: Int ->
    Route(
        routeId = rs.getLong("id"),
        arrivalTime = rs.getTimestamp("arrival_time").toInstant(),
        departureTime = rs.getTimestamp("departure_time").toInstant(),
        arrivalStation = rs.getString("arrival_station"),
        departureStation = rs.getString("departure_station")
    )
}

val TRAIN_MAPPER = RowMapper { rs, _ ->
    Train(
        trainId = rs.getLong("id"),
        trainNum = rs.getLong("train_num"),
        routeId = rs.getLong("routeId")
    )
}

val VAN_MAPPER = RowMapper { rs, _ ->
    Van(
        vanId = rs.getLong("id"),
        vanType = VanType.from(rs.getInt("van_type")) ?: VanType.UNDEFINED,
        trainId = rs.getLong("train_id"),
        vanNum = rs.getLong("van_num")
    )
}

val SEAT_MAPPER = RowMapper { rs, _ ->
    Seat(
        seatId = rs.getLong("id"),
        vanId = rs.getLong("van_id")
    )
}

val TICKET_MAPPER = RowMapper { rs, _ ->
    getTicketConstructor(rs)
}

val TICKET_INFO_MAPPER = RowMapper { rs, _ ->
    TicketInfo(
        getTicketConstructor(rs),
        Receipt(
            ticketId = rs.getLong("id"),
            userId = rs.getLong("user_id"),
            transactionId = rs.getLong("transaction_id"),
            transactionStatus = TransactionStatus.from(rs.getInt("transaction_status")) ?: TransactionStatus.UNDEFINED
        )
    )
}

val STATION_MAPPER = RowMapper { rs, _ ->
    Station(
        id = rs.getLong("id"),
        name = rs.getString("name")
    )
}

private fun getTicketConstructor(rs: ResultSet) = Ticket(
    ticketId = rs.getLong("id"),
    userId = rs.getLong("user_id"),
    name = rs.getString("name"),
    surname = rs.getString("surname"),
    routeId = rs.getLong("route_id"),
    trainId = rs.getLong("train_id"),
    vanId = rs.getLong("van_id"),
    seatId = rs.getLong("seat_id")
)