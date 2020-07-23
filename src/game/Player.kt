package game

import items.Item
import map.Room
import java.util.*

class Player(var currentRoom: Room) {
    val inventory = TreeMap<String, Item>(String.CASE_INSENSITIVE_ORDER)
    //val inventory = HashMap<String, Item>()
}