package map

import data.RoomData
import data.SavableRoomData
import data.SavableItemData
import game.GameResult
import game.GameResultCode
import items.*

class RoomWithBowl(private val bonzo: Bonzo) : Room("roomWithBowl"), SavableRoom {
    private val bowl = Bowl(this, this::addBonzoToBowl)

    init {
        baseDescription = "This room only has a bowl in it."
        bonzo.bowl = bowl
        addItem(bowl)
    }

    private fun addBonzoToBowl(): GameResult {
        addItem(bonzo)
        return GameResult(GameResultCode.SUCCESS, "You discover [${bonzo.name}] in the bowl.")
    }

    override fun save(gameItems: ItemMap) {
        gameItems.add(bowl)
        gameItems.add(bonzo)
    }

    override fun loadRoom(roomData: RoomData, gameItems: ItemMap) {
        items.clear()
        for (itemData in roomData.itemsData) {
            val item = gameItems[itemData.name]
            if (item is SavableItem) {
                item.loadItem(itemData as SavableItemData)
            }
            item?.let {
                items.add(item)
            }
        }
    }
}