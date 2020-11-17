package map

import game.Constraint
import game.GameResult
import game.GameResultCode
import game.Player
import items.*

class RoomWithHatch(roomId: String, description: String, private val hatch: Hatch): Room(roomId, description) {
    init {
        addItem(hatch)
        addConstraint(Direction.UP, Constraint(hatch::isTooHigh, "This [${hatch.name}] is out of reach!"))
    }

    override fun peek(player: Player, direction: Direction): GameResult {
        if (direction == Direction.UP && hatch.isTooHigh) {
            return GameResult(GameResultCode.FAIL, "You can't quite peek into the room above when the [${hatch.name}] is so high up there.")
        }

        return super.peek(player, direction)
    }
}