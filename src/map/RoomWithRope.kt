package map

import data.SerializableRoomData
import data.ItemData
import items.*

class RoomWithRope : Room("roomWithRope"), SavableRoom {
    private val rope = Rope()

    init {
        addItem(rope)
    }

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