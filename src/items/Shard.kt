package items

import game.GameResult
import game.GameResultCode
import player.Player

class Shard : Item("Shard", "This is shiny.") {
    enum class Error { NOT_IN_INVENTORY }

    override fun take(player: Player): GameResult {
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }

    fun getErrorMessage(error: Error): String {
        when (error) {
            Error.NOT_IN_INVENTORY -> return "You can't peek anywhere without something shiny...!!"
        }
    }
}