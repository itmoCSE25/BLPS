package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Seat
import com.itmo.blss.service.SeatsDbService
import com.itmo.blss.service.VansDbService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class SeatsService(
    private val seatsDbService: SeatsDbService,
    private val vansDbService: VansDbService
) {

    @Transactional
    fun addSeats(seats: List<Seat>) {
        for (seat in seats) {
            val van = vansDbService.getVan(seat.vanId)
            if (seatsDbService.getSeatsCount(seat.vanId) == van.vanType.maxSeatCount) {
                throw IllegalArgumentException("Max seat count is reached")
            }
        }
        seatsDbService.addSeats(seats)
    }
}
