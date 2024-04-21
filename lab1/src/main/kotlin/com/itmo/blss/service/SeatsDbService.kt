package com.itmo.blss.service

import com.itmo.blss.model.db.Seat

interface SeatsDbService {

    fun getSeatsByVanId(vanId: Long): List<Seat>

    fun addSeats(seats: List<Seat>)

    fun getSeatsCount(vanId: Long): Long
}