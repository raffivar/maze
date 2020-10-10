package actions

import game.GameResult
import game.GameResultCode
import game.Player
import map.Direction

class Peek : Action("Peek", "Peek [direction], given a certain item") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose a direction.")
        }
        val directionAsText = args[0]
        val direction = Direction.values().find { it.name.equals(directionAsText, true) } ?: return GameResult(
            GameResultCode.ERROR, "[$directionAsText] is not a valid direction."
        )
        return when (player.inventory.containsKey("shard")) {
            false -> GameResult(GameResultCode.ERROR, "You can't peek anywhere without something shiny.")
            true -> {
                val peekResult = player.currentRoom.peek(player, direction)
                when (peekResult.gameResultCode) {
                    GameResultCode.SUCCESS -> GameResult(peekResult.gameResultCode, "Peeking [${direction.name}]:\n${peekResult.message}")
                    else -> peekResult
                }
            }
        }
    }
}