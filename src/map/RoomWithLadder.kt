package map

import data.SerializableRoomData
import data.ItemData
import items.*

class RoomWithLadder(private val ladder: Ladder): Room("roomWithLadder"), SavableRoom {
    init {
        addItem(ladder)
    }

    override fun saveRoomDataToDB(gameItems: ItemMap) {
        gameItems.addItem(ladder)
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