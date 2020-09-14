package game

import data.ItemData
import data.PlayerData
import data.SavableObject
import items.ItemMap
import map.Room

class Player(var currentRoom: Room) : SavableObject {
    val inventory = ItemMap()

    override fun saveIntoGameData(gameData: GameData) {
        val roomId = currentRoom
        val items = ArrayList<ItemData>()
        for (item in inventory.values) {
            items.add(ItemData(item.name, item.description))
        }
    }

    fun getData(): PlayerData {
        val itemsData = ArrayList<ItemData>()
        for (item in inventory.values) {
            itemsData.add(ItemData(item.name, item.description))
        }
        return PlayerData(currentRoom.roomId, itemsData)
    }
}