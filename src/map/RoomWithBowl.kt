package map

import data.SerializableRoomData
import data.ItemData
import game.GameResult
import game.GameResultCode
import items.*

class RoomWithBowl(private val poison: Poison) : Room("roomWithBowl"), SavableRoom {
    private val bowl = Bowl(this, this::addPoisonToBowl)

    init {
        baseDescription = "This room only has a bowl in it."
        poison.bowl = bowl
        addItem(bowl)
    }

    private fun addPoisonToBowl(): GameResult {
        addItem(poison)
        return GameResult(GameResultCode.SUCCESS, "You discover [${poison.name}] in the bowl.")
    }

    override fun save(gameItems: ItemMap) {
        gameItems.add(bowl)
        gameItems.add(poison)
    }

    override fun loadRoom(roomData: SerializableRoomData, gameItems: ItemMap) {
        items.clear()
        for (itemData in roomData.itemsData) {
            val item = gameItems[itemData.name]
            item?.let {
                items.add(item)
            }
            if (item is SavableItem) {
                item.loadItem(itemData as ItemData)
            }
        }
    }
}