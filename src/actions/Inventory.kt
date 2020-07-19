package actions

import game.GameResult
import game.Player
import game.GameResultCode

class Inventory : Action("Inventory", "Inventory") {
    override fun execute(player: Player, args: List<String>): GameResult {
        return player.getInventory()
    }
}