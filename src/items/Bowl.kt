package items

import game.GameResult
import game.GameResultCode
import map.Room

class Bowl(val room: Room, private val itemToAdd: Item) : Item("Bowl", "This is a bowl") {
    override fun examine(): GameResult {
        room.addItem(itemToAdd)
        room.removeItem(this) //Remove this item
        room.addItem(Item(name, desc)) //And then add this item again, as a regular item this time
        return GameResult(GameResultCode.SUCCESS, "You discover [${itemToAdd.name}] in the bowl")
    }
}