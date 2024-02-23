package com.itmo.blss.api

import com.itmo.blss.api.response.VanResponse
import com.itmo.blss.model.db.Van
import com.itmo.blss.service.VansDbService
import com.itmo.blss.utils.ApiConstraints.Companion.TRAIN_ID_KEY
import org.springframework.web.bind.annotation.GetMapping
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
    ): List<VanResponse> {
        return vansDbService.getVansByTrainId(trainId)
            .map { it.toVanResponse() }
    }

    private fun Van.toVanResponse() = VanResponse(
        vanType = this.vanType,
        trainId = this.trainId
    )
}