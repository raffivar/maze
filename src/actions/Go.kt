package actions

import game.GameResult
import game.Player
import game.GameResultCode
import map.Direction
import map.Exit

class Go : Action("Go", "Go [direction]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose a direction")
        }
        val directionAsText = args[0]
        val direction = Direction.values().find { it.name.equals(directionAsText, true) } ?: return GameResult(
            GameResultCode.ERROR,
            "Invalid direction"
        )
        return player.go(direction)
    }
}