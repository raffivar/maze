package map

import data.SerializableRoomData
import items.ItemMap

interface SavableRoom {
    fun save(gameItems: ItemMap)
    fun loadRoom(roomData: SerializableRoomData, gameItems: ItemMap)
}