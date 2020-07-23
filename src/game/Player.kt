package game

import items.ItemMap
import map.Room

class Player(var currentRoom: Room) {
    val inventory = ItemMap()
}