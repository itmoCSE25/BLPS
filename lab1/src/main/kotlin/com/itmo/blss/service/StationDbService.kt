package com.itmo.blss.service

import com.itmo.blss.model.db.Station

interface StationDbService {

    fun getStations(): List<Station>

    fun getStationByName(name: String): Station

    fun isStationsExist(ids: List<Long>): Boolean

    fun createStation(station: Station)

    fun upsertStations(stations: List<Station>)
}
