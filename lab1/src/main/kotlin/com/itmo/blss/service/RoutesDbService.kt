package com.itmo.blss.service

import com.itmo.blss.model.RoutesFilter
import com.itmo.blss.model.db.Route

interface RoutesDbService {

    fun getRoutesWithFilter(routesFilter: RoutesFilter): List<Route>

    fun isRouteExist(routeId: Long): Boolean
}