package map

import data.ItemData
import data.SerializableRoomData
import game.Constraint
import items.*

class RoomWithWindow(rope: Rope): Room("roomWithWindow", "With a little help, you might be able to sneak down outside through that window!"), SavableRoom {
    private val pole = RopeDependedItem("Pole", rope)
    private val window = RopeDependedItem("Window", rope)

    init {
        addItem(pole)
        addItem(window)
        addConstraint(Direction.DOWN, Constraint(pole::hasNothingAttached, "Try finding something to attach to the pole first?"))
        addConstraint(Direction.DOWN, Constraint(window::hasNothingAttached, "Try throwing the [${rope.name}] out of the window first?"))
    }

    override fun saveRoom(gameItems: ItemMap) {
        gameItems.add(pole)
        gameItems.add(window)
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