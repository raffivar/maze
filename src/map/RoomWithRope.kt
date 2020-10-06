package map

import data.SerializableRoomData
import data.ItemData
import game.GameResult
import game.GameResultCode
import items.*

class RoomWithRope(private val rope: Rope) : Room("roomWithRope"), SavableRoom {
    override fun saveRoom(gameItems: ItemMap) {
        gameItems.add(rope)
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