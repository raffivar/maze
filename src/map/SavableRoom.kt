package map

import data.RoomData
import items.ItemMap

interface SavableRoom {
    fun saveRoom(gameItems: ItemMap)
    fun loadRoom(roomData: RoomData, gameItems: ItemMap)
}