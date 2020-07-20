package actions

import game.GameResult
import game.Player
import game.GameResultCode

class Open : Action("Open", "Open [item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose an item in the room to open")
        }
        val itemName = args[0]
        return player.open(itemName)
    }
}