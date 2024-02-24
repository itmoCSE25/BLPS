package com.itmo.blss.utils

import com.itmo.blss.model.db.Route
import com.itmo.blss.model.db.Seat
import com.itmo.blss.model.db.Ticket
import com.itmo.blss.model.db.Train
import com.itmo.blss.model.db.Van
import com.itmo.blss.model.enums.VanType
import org.springframework.jdbc.core.RowMapper

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
        trainNum = rs.getInt("train_num")
    )
}

val VAN_MAPPER = RowMapper { rs, _ ->
    Van(
        vanId = rs.getLong("id"),
        vanType = VanType.from(rs.getInt("van_type")) ?: VanType.UNDEFINED,
        trainId = rs.getLong("train_id")
    )
}

val SEAT_MAPPER = RowMapper { rs, _ ->
    Seat(
        seatId = rs.getLong("id"),
        vanId = rs.getLong("van_id")
    )
}

val TICKET_MAPPER = RowMapper { rs, _ ->
    Ticket(
        ticketId = rs.getLong("id"),
        userId = rs.getLong("user_id"),
        name = rs.getString("name"),
        surname = rs.getString("surname"),
        routeId = rs.getLong("route_id"),
        trainId = rs.getLong("train_id"),
        vanId = rs.getLong("van_id"),
        seatId = rs.getLong("seat_id")
    )
}