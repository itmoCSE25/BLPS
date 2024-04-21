package com.itmo.blss.service

import com.itmo.blss.model.create.CreateRouteDto

interface RoutesService {

    fun createRoute(createRouteDto: CreateRouteDto)
}