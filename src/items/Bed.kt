package items

import game.GameResult
import game.GameResultCode
import map.Room

class Bed(val room: Room, private val key: Key) : Item("Bed", "This is a bed") {
    override fun examine(): GameResult {
        room.addItem(key)
        room.removeItem(this)
        room.addItem(Item(name, desc))
        return GameResult(GameResultCode.SUCCESS, "You discover [${key.name}] under this bed")
    }
}