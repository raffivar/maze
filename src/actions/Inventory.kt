package actions

import GameResult
import Player

class Inventory(args: List<String>) : Action("Inventory", args) {
    override fun execute(player: Player): GameResult {
            return if (player.inventory.isEmpty()) {
                GameResult(GameResult.GameResultCode.OK, "[Inventory is currently empty]")
            } else {
                GameResult(GameResult.GameResultCode.OK, "Inventory: ${player.inventory.keys}")
        }
    }
}