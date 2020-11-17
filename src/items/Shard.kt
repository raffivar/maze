package items

import game.GameResult
import game.GameResultCode
import player.Player

class Shard() : Item("Shard", "This is shiny.") {
    override fun take(player: Player): GameResult {
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }
}