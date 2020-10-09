package items

import game.GameResult
import game.GameResultCode
import game.Player

class Key(private val executeWhenTaken: () -> Unit) : Item("Key", "A regular, generic, dusty, key.") {
    override fun take(player: Player): GameResult {
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        executeWhenTaken.invoke()
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }
}