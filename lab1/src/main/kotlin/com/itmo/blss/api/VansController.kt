package com.itmo.blss.api

import com.itmo.blss.api.response.VanDto
import com.itmo.blss.model.create.CreateVanDto
import com.itmo.blss.model.db.Van
import com.itmo.blss.service.VansDbService
import com.itmo.blss.utils.ApiConstraints.Companion.TRAIN_ID_KEY
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vans")
class VansController(
    private val vansDbService: VansDbService
) {

    @GetMapping
    fun getVans(
        @RequestParam(value = TRAIN_ID_KEY)
        trainId: Long
    ): ResponseEntity<List<VanDto>> {
        return ResponseEntity.ok(
            vansDbService.getVansByTrainId(trainId)
                .map { it.toVanResponse() }
        )
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun createVans(
        @RequestBody createVanDto: CreateVanDto
    ): ResponseEntity<Void> {
        vansDbService.createVan(createVanDto.toModel())
        return ResponseEntity.ok().build();
    }

    private fun Van.toVanResponse() = VanDto(
        vanId = this.vanId,
        vanType = this.vanType,
        trainId = this.trainId
    )

    private fun CreateVanDto.toModel() = Van(
        trainId = this.trainId,
        vanType = this.vanType,
        vanNum = this.vanNum
    )
}