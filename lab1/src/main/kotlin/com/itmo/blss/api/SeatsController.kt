package com.itmo.blss.api

import com.itmo.blss.api.response.SeatDto
import com.itmo.blss.model.create.CreateSeatsDto
import com.itmo.blss.model.db.Seat
import com.itmo.blss.service.SeatsDbService
import com.itmo.blss.service.impl.SeatsService
import com.itmo.blss.utils.ApiConstraints.Companion.VAN_ID_KEY
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/seats")
class SeatsController(
    private val seatsDbService: SeatsDbService,
    private val seatsService: SeatsService,
) : JavaDelegate {

    @GetMapping
    fun getSeatsWithFilter(
        @RequestParam(value = VAN_ID_KEY)
        vanId: Long
    ): ResponseEntity<List<SeatDto>> {
        return ResponseEntity.ok(
            seatsDbService.getSeatsByVanId(vanId)
                .map { it.toSeatResponse() }
        )
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun addSeats(
        @RequestBody createSeatsDto: List<CreateSeatsDto>
    ): ResponseEntity<Void> {
        seatsService.addSeats(createSeatsDto.map { it.toModel() })
        return ResponseEntity.ok().build()
    }

    private fun Seat.toSeatResponse() = SeatDto(
        vanId = this.vanId,
        seatId = this.seatId
    )

    private fun CreateSeatsDto.toModel() = Seat(
        vanId = this.vanId
    )

    override fun execute(p0: DelegateExecution) {
        val vanId: Long = p0.getVariable("van") as Long
        val res = seatsDbService.getSeatsByVanId(vanId)
        p0.setVariable("seats", res.map { it.seatId })
    }
}
