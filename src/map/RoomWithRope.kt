package map

import items.*

class RoomWithRope(rope: Rope) : Room("roomWithRope") {
    init {
        addItem(rope)
    }
}