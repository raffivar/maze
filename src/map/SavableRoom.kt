package map

import data.SerializableRoomData
import items.ItemMap

interface SavableRoom {
    fun saveRoomDataToDB(gameItems: ItemMap)
    fun loadFromDB(roomData: SerializableRoomData, gameItems: ItemMap)
}