package actions

import game.GameResult
import game.GameResultCode
import player.Player

class Examine : Action("Examine", "Examine [room/item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose the subject (room/item) to examine")
        }
        val subject = args[0]
        if (subject == "room") {
            return player.currentRoom.examine()
        }
        val item = player.inventory[subject] ?: player.currentRoom.items[subject] ?: return GameResult(
            GameResultCode.FAIL,
            "No [$subject] in inventory or current room."
        )
        return item.examine()
    }
}