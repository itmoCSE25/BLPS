package com.itmo.blss.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trains")
class TrainsController {

    @GetMapping
    fun getTrains() {

    }
}