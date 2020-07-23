package items

import game.GameResult
import game.GameResultCode
import game.Player

class Bonzo : Item("Bonzo", "Can be used on dogs") {
    override fun take(player: Player): GameResult {
        player.inventory[this.name] = this
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}]")
    }
}