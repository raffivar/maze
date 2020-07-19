package actions

import game.GameResult
import game.Player
import game.GameResultCode
import items.Takable

class Take : Action("Take", "Take [item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(
                GameResultCode.ERROR,
                "Please choose an item in the room to take"
            )
        }
        val itemName = args[0]
        return player.take(itemName)
    }
}