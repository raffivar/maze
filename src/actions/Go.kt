package actions

import game.GameResult
import player.Player
import game.GameResultCode
import map.directions.Direction

class Go : Action("Go", "Go [direction]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose a direction.")
        }
        val directionAsText = args[0]
        val direction = Direction.values().find { it.name.equals(directionAsText, true) } ?: return GameResult(
            GameResultCode.ERROR, "[$directionAsText] is not a valid direction."
        )

        val moveResult = player.currentRoom.move(player, direction)

        return when (moveResult.gameResultCode) {
            GameResultCode.ERROR, GameResultCode.FAIL, GameResultCode.GAME_OVER  ->  {
                moveResult
            }
            GameResultCode.SUCCESS -> {
                player.currentRoom.triggerEntranceEvent(moveResult)
            }
        }
    }
}