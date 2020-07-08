package actions

import GameResult
import Player
import map.Direction
import map.Exit

class Go(args: List<String>) : Action("Go", args) {
    override fun execute(player: Player): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResult.GameResultCode.ERROR, "Please choose a direction")
        }
        val directionAsText = args[0]
        val direction =
            Direction.values().find { it.name.equals(directionAsText, true) } ?: return GameResult(GameResult.GameResultCode.ERROR, "Invalid direction")
        val nextRoom =
            player.currRoom.rooms[direction] ?: return GameResult(GameResult.GameResultCode.OK, "This room does not lead to that direction")
        player.currRoom = nextRoom
        return if (player.currRoom is Exit) {
            GameResult(GameResult.GameResultCode.GAME_OVER, "You're out! Congrats!")
        } else {
            GameResult(GameResult.GameResultCode.OK, "You moved ${direction.name}\n${player.currRoom.getDescription()}")
        }
    }

}