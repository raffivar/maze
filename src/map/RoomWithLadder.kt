package map

import data.SerializableRoomData
import data.ItemData
import items.*

class RoomWithLadder: Room("roomWithLadder"), SavableRoom {
    private val ladder = Ladder()

    init {
        addItem(ladder)
    }

    override fun saveRoom(gameItems: ItemMap) {
        gameItems.add(ladder)
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