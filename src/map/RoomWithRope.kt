package map

import data.SerializableRoomData
import data.ItemData
import items.*

class RoomWithRope(private val rope: Rope) : Room("roomWithRope"), SavableRoom {
    init {
        addItem(rope)
    }

    override fun saveRoomDataToDB(gameItems: ItemMap) {
        gameItems.addItem(rope)
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