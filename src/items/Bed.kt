package items

import game.GameResult
import game.GameResultCode
import map.Room

class Bed(val room: Room, private val key: Key) : Item("Bed", "This is a bed") {
    override fun examine(): GameResult {
        room.addItem(key)
        room.removeItem(this) //Remove this item
        room.addItem(Item(name, desc)) //And then add this item again, as a regular item this time
        return GameResult(GameResultCode.SUCCESS, "You discover [${key.name}] under this bed")
    }
}