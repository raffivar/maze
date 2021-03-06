package actions

import game.GameResult
import player.Player
import game.GameResultCode

class Drop : Action("Drop", "Drop [item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Invalid arguments. Please use the format: '$howToUse'.")
        }
        val itemName = args[0]
        val item = player.inventory[itemName] ?: return GameResult(
            GameResultCode.FAIL, "No [$itemName] in inventory."
        )
        return item.drop(player)
    }
}