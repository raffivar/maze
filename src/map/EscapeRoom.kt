package map

import data.ItemData
import data.SerializableRoomData
import game.Constraint
import items.*

class EscapeRoom(rope: Rope): Room("escapeRoom", "With a little help, you might be able to sneak down outside through that window!"), SavableRoom {
    private val pole = Pole("Pole", rope)
    private val window = Window("Window", pole)

    init {
        addItem(pole)
        addItem(window)
        addConstraint(Direction.DOWN, Constraint(pole::hasNothingAttached, "You can't really jump from there, it's too high."))
        addConstraint(Direction.DOWN, Constraint(window::hasNothingAttached, "Man, you already tied the [${rope.name}] to the [${pole.name}]. Think a bit more."))
    }

    override fun saveRoom(gameItems: ItemMap) {
        gameItems.addItem(pole)
        gameItems.addItem(window)
    }

    override fun loadRoom(roomData: SerializableRoomData, gameItems: ItemMap) {
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