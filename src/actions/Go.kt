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

        val nextRoom = player.currentRoom.rooms[direction] ?: return GameResult(
            GameResultCode.FAIL,
            "This room does not lead [$direction]"
        )

        val constraints = player.currentRoom.constraintsToMoveOrPeek[direction]
        constraints?.let {
            for (constraint in it) {
                if (constraint.isConstraining.invoke()) {
                    return GameResult(GameResultCode.FAIL, "Cannot move [${direction.name}] - ${constraint.message}")
                }
            }
        }

        var eventsMessage = ""
        val events = player.currentRoom.eventsUponMovement[direction]
        events?.let {
            for (event in it) {
                val eventResult = event.invoke(direction)
                when (eventResult.gameResultCode) {
                    GameResultCode.GAME_OVER -> return eventResult
                    else -> {
                        if (eventResult.message.isNotBlank()) {
                            eventsMessage += eventResult.message
                        }
                    }
                }
            }
        }

        player.currentRoom = nextRoom

        val moveMessage = "Moved [${direction.name}]."
        val moveResult = when (eventsMessage.isBlank()) {
            true -> GameResult(GameResultCode.SUCCESS, moveMessage)
            false -> GameResult(GameResultCode.SUCCESS, "$eventsMessage\n$moveMessage")
        }

        return when (moveResult.gameResultCode) {
            GameResultCode.ERROR, GameResultCode.FAIL, GameResultCode.GAME_OVER -> moveResult
            GameResultCode.SUCCESS -> {
                player.currentRoom.triggerEntranceEvent(moveResult)
            }
        }
    }
}