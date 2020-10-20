package items

import game.GameResult
import game.GameResultCode
import game.Player

class Shard(private val executeWhenTaken: () -> Unit) : Item("Shard", "This is shiny.") {
    override fun take(player: Player): GameResult {
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        executeWhenTaken.invoke()
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }
}