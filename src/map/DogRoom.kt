package map

import data.SerializableRoomData
import data.SavableItemData
import items.ItemMap
import items.SavableItem

class DogRoom(roomId: String): Room(roomId), SavableRoom {
    override fun save(gameItems: ItemMap) {
        val dog = items["Dog"]
        dog?.let {
            gameItems.add(dog)
        }
    }

    override fun loadRoom(roomData: SerializableRoomData, gameItems: ItemMap) {
        items.clear()
        for (itemData in roomData.itemsData) {
            val item = gameItems[itemData.name]
            item?.let {
                items.add(item)
            }
            if (item is SavableItem) {
                item.loadItem(itemData as SavableItemData)
            }
        }
    }
}