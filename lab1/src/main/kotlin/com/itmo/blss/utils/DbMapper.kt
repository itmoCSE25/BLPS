package com.itmo.blss.utils

import com.itmo.blss.model.db.Route
import com.itmo.blss.model.db.Seat
import com.itmo.blss.model.db.Train
import com.itmo.blss.model.db.Van
import com.itmo.blss.model.enums.VanType
import org.springframework.jdbc.core.RowMapper

val ROUTE_MAPPER = RowMapper { rs, _: Int ->
    Route(
        arrivalTime = rs.getTimestamp("arrival_time").toInstant(),
        departureTime = rs.getTimestamp("departure_time").toInstant(),
        arrivalStation = rs.getString("arrival_station"),
        departureStation = rs.getString("departure_station")
    )
}

val TRAIN_MAPPER = RowMapper { rs, _ ->
    Train(
        trainNum = rs.getInt("train_num")
    )
}

val VAN_MAPPER = RowMapper { rs, _ ->
    Van(
        vanType = VanType.from(rs.getInt("van_type")) ?: VanType.UNDEFINED,
        trainId = rs.getLong("train_id")
    )
}

val SEAT_MAPPER = RowMapper { rs, _ ->
    Seat(
        vanId = rs.getLong("van_id"),
        ticketId = rs.getLong("ticket_id")
    )
}