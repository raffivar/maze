package items

import game.GameResult
import game.GameResultCode
import items.interfaces.Reflective
import player.Player

class Shard : Item("Shard", "A piece of mirror. Might be useful."), Reflective {
    override fun take(player: Player): GameResult {
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }
}