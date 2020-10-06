package map

import data.SerializableRoomData
import items.ItemMap

interface SavableRoom {
    fun saveRoom(gameItems: ItemMap)
    fun loadRoom(roomData: SerializableRoomData, gameItems: ItemMap)
}