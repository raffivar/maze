package map

import game.Constraint
import items.*

class RoomWithHatch(roomId: String, description: String, hatch: Hatch): Room(roomId, description) {
    init {
        addItem(hatch)
        addConstraint(Direction.UP, Constraint(hatch::isTooHigh, "This [${hatch.name}] is out of reach!"))
    }
}