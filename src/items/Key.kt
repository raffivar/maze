package items

import game.GameResult
import game.GameResultCode
import game.Player

class Key : Item("Key", "This is a key") {
    override fun take(player: Player): GameResult {
        player.inventory[this.name] = this
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}]")
    }
}