package actions

import game.GameResult
import game.Player
import game.GameResultCode

class Inventory : Action("Inventory") {
    override fun execute(player: Player, args: List<String>): GameResult {
        return if (player.inventory.isEmpty()) {
            GameResult(GameResultCode.SUCCESS, "[Inventory is currently empty]")
        } else {
            GameResult(GameResultCode.SUCCESS, "Inventory: ${player.inventory.keys}")
        }
    }
}