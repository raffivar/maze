package map

import data.RoomData

interface SavableRoom {
    fun saveRoom(mapBuilder: MapBuilder)
    fun loadRoom(roomData: RoomData)
}