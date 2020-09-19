package map

import data.RoomData
import data.SavableRoomData
import items.ItemMap

interface SavableRoom {
    fun save(gameItems: ItemMap)
    fun loadRoom(roomData: RoomData, gameItems: ItemMap)
}