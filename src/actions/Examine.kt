package actions

import game.GameResult
import game.GameResultCode
import game.Player

class Examine : Action("Examine", "Examine [room/item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(
                GameResultCode.ERROR,
                "Please choose either the room or an item to examine"
            )
        }
        val objectToExamine = args[0]
        return player.examine(objectToExamine)
    }
}