package map.rooms

import items.*
import map.rooms.Room

class RoomWithLadder(ladder: Ladder): Room("roomWithLadder") {
    init {
        addItem(ladder)
    }
}