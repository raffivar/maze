package actions

import game.GameResult
import player.Player
import game.GameResultCode

class Take : Action("Take", "Take [item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Invalid arguments. Please use the format: '$howToUse'.")
        }
        val itemName = args[0]
        val item = player.currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL, "No [$itemName] in current room."
        )
        return item.take(player)
    }
}