package player

import data.serializables.SerializableItemData
import data.player.PlayerData
import items.maps.ItemMap
import map.rooms.Room

class Player(var currentRoom: Room) {
    val inventory = ItemMap()
    
    fun getBaseData(): PlayerData {
        val itemsData = ArrayList<SerializableItemData>()
        for (item in inventory.values) {
            itemsData.add(item.getData())
        }
        return PlayerData(currentRoom.roomId, itemsData)
    }
}