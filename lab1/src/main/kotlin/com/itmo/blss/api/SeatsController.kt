package com.itmo.blss.api

import com.itmo.blss.api.response.SeatResponse
import com.itmo.blss.model.db.Seat
import com.itmo.blss.service.SeatsDbService
import com.itmo.blss.utils.ApiConstraints.Companion.VAN_ID_KEY
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/seats")
class SeatsController(
    private val seatsDbService: SeatsDbService
) {

    @GetMapping
    fun getSeatsWithFilter(
        @RequestParam(value = VAN_ID_KEY)
        vanId: Long
    ): List<SeatResponse> {
        return seatsDbService.getSeatsByVanId(vanId)
            .map { it.toSeatResponse() }
    }

    private fun Seat.toSeatResponse() = SeatResponse(
        vanId = this.vanId,
        seatId = this.seatId
    )
}