package items

import game.GameResult
import game.GameResultCode
import game.Player

class Shard(private val executeWhenTaken: () -> Unit) : Item("Shard", "Shiny!!!!!!") {
    override fun take(player: Player): GameResult {
        player.inventory.add(this)
        player.currentRoom.removeItem(this)
        executeWhenTaken.invoke()
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }
}