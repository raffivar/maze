package map

import data.RoomData
import items.ItemMap

interface SavableRoom {
    fun save(gameItems: ItemMap)
    fun loadRoom(roomData: RoomData, gameItems: ItemMap)
}