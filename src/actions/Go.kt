package actions

import game.GameResult
import game.Player
import game.GameResultCode
import map.Direction
import map.Exit

class Go : Action("Go") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(
                GameResultCode.ERROR,
                "Please choose a direction"
            )
        }
        val directionAsText = args[0]
        val direction =
            Direction.values().find { it.name.equals(directionAsText, true) } ?: return GameResult(
                GameResultCode.ERROR,
                "Invalid direction"
            )
        val nextRoom =
            player.currRoom.rooms[direction] ?: return GameResult(
                GameResultCode.OK,
                "This room does not lead to that direction"
            )
        player.currRoom = nextRoom
        return if (player.currRoom is Exit) {
            GameResult(
                GameResultCode.GAME_OVER,
                "You're out! Congrats!"
            )
        } else {
            GameResult(
                GameResultCode.OK,
                "You moved ${direction.name}\n${player.currRoom.getDescription()}"
            )
        }
    }

}