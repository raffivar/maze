package map

import items.*

class RoomWithLadder(ladder: Ladder): Room("roomWithLadder") {
    init {
        addItem(ladder)
    }
}