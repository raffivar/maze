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
        baseDescription = "This room only has a [${closet.name}] in it."
    }

    private fun addPoisonToRoom(): GameResult {
        addItem(poison)
        return GameResult(GameResultCode.SUCCESS, "You discover [${poison.name}] in the [${closet.name}].")
    }

    override fun saveRoom(gameItems: ItemMap) {
        gameItems.addItem(closet)
        gameItems.addItem(poison)
    }

    override fun loadRoom(roomData: SerializableRoomData, gameItems: ItemMap) {
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