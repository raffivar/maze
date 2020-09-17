package data

import map.MapBuilder

interface SavableRoom {
    fun saveRoomData(mapBuilder: MapBuilder)
}