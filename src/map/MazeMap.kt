package map

import java.util.*

class MazeMap : TreeMap<String, Room>(String.CASE_INSENSITIVE_ORDER) {
    fun add(room: Room) {
        this[room.roomId] = room
    }
}