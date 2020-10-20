package map

import data.SerializableRoomData
import data.ItemData
import game.GameResult
import game.GameResultCode
import items.*

class RoomWithCloset(private val poison: Poison) : Room("RoomWithCloset"), SavableRoom {
    private var closet: Closet

    init {
        closet = Closet(true, this::addPoisonToRoom)
        addItem(closet)
        baseDescription = "This room nothing but a [${closet.name}] in it."
        addItem(Door(false))
    }

    private fun addPoisonToRoom(): GameResult {
        addItem(poison)
        return GameResult(GameResultCode.SUCCESS, "Opened the [${closet.name}]. You discover some [${poison.name}] inside.")
    }

    override fun saveRoomDataToDB(gameItems: ItemMap) {
        gameItems.addItem(closet)
        gameItems.addItem(poison)
    }

    override fun loadFromDB(roomData: SerializableRoomData, gameItems: ItemMap) {
        items.clear()
        for (itemData in roomData.itemsData) {
            val item = gameItems[itemData.name]
            item?.let {
                items.addItem(item)
            }
            if (item is SavableItem) {
                item.loadItem(itemData as ItemData)
            }
        }
    }
}