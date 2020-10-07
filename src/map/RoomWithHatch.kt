package map

import items.*

class RoomWithHatch(hatch: Hatch): Room("roomWithLadder") {
    init {
        addItem(hatch)
    }
}