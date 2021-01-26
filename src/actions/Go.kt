package actions

import game.GameResult
import player.Player
import game.GameResultCode
import map.directions.Direction
import map.rooms.interfaces.EnterEventRoom
import map.rooms.interfaces.ExitEventRoom

class Go : Action("Go", "Go [direction]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Invalid arguments. Please use the format: '$howToUse'.")
        }

        val directionAsText = args[0]
        val direction = Direction.values().find { it.name.equals(directionAsText, true) } ?: return GameResult(
            GameResultCode.ERROR, "[$directionAsText] is not a valid direction."
        )

        val roomToMove = player.currentRoom.rooms[direction] ?: return GameResult(
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

        val roomMovedFrom = player.currentRoom
        var moveMessage = "Moved [${direction.name}]."

        val onRoomExitEvent = when (roomMovedFrom is ExitEventRoom) {
            true -> roomMovedFrom.onRoomExited(direction, player)
            false -> null
        }

        onRoomExitEvent?.let {
            when (it.gameResultCode) {
                GameResultCode.SUCCESS -> moveMessage = "${it.message}\n$moveMessage"
                else -> return it
            }
        }

        player.currentRoom = roomToMove
        val examineResult = roomToMove.examine()
        val defaultResult = examineResult.createByPrefix(moveMessage)

        return when (roomToMove is EnterEventRoom) {
            true -> roomToMove.onRoomEntered(defaultResult, player)
            false -> defaultResult
        }
    }
}