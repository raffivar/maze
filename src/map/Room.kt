package map

import items.Item
import java.util.*
import kotlin.collections.HashMap

open class Room {
    var desc: String = "Just a regular room"
    val items =  TreeMap<String, Item>(String.CASE_INSENSITIVE_ORDER)
    //val items = HashMap<String, Item>()
    val rooms = HashMap<Direction, Room>()

    fun getDescription(): String {
        var description = desc + "\n"
        description += if (items.isEmpty()) {
            "This room is empty\n"
        } else {
            description += "Items in room: "
            items.keys.toString() + "\n"
        }
        description += if (rooms.isEmpty()) {
            "It seems this room doesn't lead anywhere else\n"
        } else {
            description += "This room leads: "
            rooms.keys.toString() + "\n"
        }
        return description
    }

    fun addItem(item: Item) {
        items[item.name] = item
    }

    fun removeItem(item: Item) {
        items.remove(item.name)
    }

    fun addRoom(direction: Direction, room: Room) {
        rooms[direction] = room
    }
}