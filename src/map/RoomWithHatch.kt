package map

import data.ItemData
import data.SerializableRoomData
import game.Constraint
import items.*

class RoomWithHatch(roomId: String, hatch: Hatch): Room(roomId), SavableRoom {
    init {
        addItem(hatch)
        addConstraint(Direction.UP, Constraint(hatch::isTooHigh, "This [${hatch.name}] is out of reach!"))
    }

    override fun saveRoom(gameItems: ItemMap) {
        for (item in items) {
            gameItems.add(item.value)
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
                item.loadItem(itemData as ItemData)
            }
        }
    }
}