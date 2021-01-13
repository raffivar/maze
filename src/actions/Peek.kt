package actions

import game.GameResult
import game.GameResultCode
import items.interfaces.Reflective
import player.Player
import map.directions.Direction

class Peek : Action("Peek", "Peek [direction] with [with], given the item is your inventory") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty() || args.size < 3 || !args[1].equals("with", true)) {
            return GameResult(GameResultCode.ERROR, "Invalid arguments. Please use the format: '$howToUse'.")
        }

        val directionName = args[0]
        val itemName = args[2]

        val direction = Direction.values().find { it.name.equals(directionName, true) } ?: return GameResult(
            GameResultCode.ERROR, "[$directionName] is not a valid direction."
        )

        val item = player.inventory[itemName] ?: player.currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL, "No [$itemName] in inventory or current room."
        )

        val roomToPeek = player.currentRoom.rooms[direction] ?: return GameResult(GameResultCode.FAIL, "This room does not lead [$direction]")

        if (item !is Reflective) {
            return GameResult(GameResultCode.ERROR, "The [${item.name}] does not seem very... reflective...")
        }

        val constraints = player.currentRoom.constraintsToMove[direction]
        constraints?.let {
            for (constraint in it) {
                if (constraint.isConstraining.invoke()) {
                    return GameResult(GameResultCode.FAIL, "Cannot peek [${direction.name}] - ${constraint.message}")
                }
            }
        }

        val peekResult = roomToPeek.peekResult(player)

        return when (peekResult.gameResultCode) {
            GameResultCode.SUCCESS -> GameResult(peekResult.gameResultCode, "Peeking [${direction.name}]:\n${peekResult.message}")
            else -> peekResult
        }
    }
}