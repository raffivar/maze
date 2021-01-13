package map.rooms

import actions.constraints.Constraint
import game.GameResult
import game.GameResultCode
import player.Player
import items.*
import map.directions.Direction

class RoomWithHatch(roomId: String, description: String, private val hatch: Hatch): Room(roomId, description) {
    init {
        addItem(hatch)
        addConstraint(Direction.UP,
            Constraint(hatch::isTooHigh, "This [${hatch.name}] is out of reach!")
        )
    }
}