package game

import data.ItemData
import data.PlayerData
import items.ItemMap
import map.Room

class Player(var currentRoom: Room) {
    val inventory = ItemMap()
    
    fun getData(): PlayerData {
        val itemsData = ArrayList<ItemData>()
        for (item in inventory.values) {
            itemsData.add(item.getData())
        }
        return PlayerData(currentRoom.roomId, itemsData)
    }
}