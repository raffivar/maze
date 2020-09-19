package game

import data.SerializableItemData
import data.PlayerData
import items.ItemMap
import map.Room

class Player(var currentRoom: Room) {
    val inventory = ItemMap()
    
    fun getData(): PlayerData {
        val itemsData = ArrayList<SerializableItemData>()
        for (item in inventory.values) {
            itemsData.add(item.getData())
        }
        return PlayerData(currentRoom.roomId, itemsData)
    }
}