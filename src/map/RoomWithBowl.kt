package map

import items.*

class RoomWithBowl : Room() {
    init {
        desc = "This room only has a bowl in it"
        val bonzo = Bonzo()
        val bowl = Bowl(this, bonzo)
        addItem(bowl)
    }
}