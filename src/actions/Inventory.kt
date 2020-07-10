package actions

import game.GameResult
import game.Player
import game.GameResultCode

class Inventory(args: List<String>) : Action("Inventory", args) {
    override fun execute(player: Player): GameResult {
            return if (player.inventory.isEmpty()) {
                GameResult(
                    GameResultCode.OK,
                    "[Inventory is currently empty]"
                )
            } else {
                GameResult(
                    GameResultCode.OK,
                    "Inventory: ${player.inventory.keys}"
                )
        }
    }
}