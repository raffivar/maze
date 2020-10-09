package map

import java.util.*
import javax.management.openmbean.KeyAlreadyExistsException

class RoomMap : TreeMap<String, Room>(String.CASE_INSENSITIVE_ORDER) {
    fun add(room: Room) {
        if (this.containsKey(room.roomId)) {
            throw KeyAlreadyExistsException("Failed to add room to room list - roomId already exists")
        }
        this[room.roomId] = room
    }
}