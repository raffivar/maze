package map.rooms

import items.*
import map.rooms.Room

class RoomWithRope(rope: Rope) : Room("roomWithRope") {
    init {
        addItem(rope)
    }
}