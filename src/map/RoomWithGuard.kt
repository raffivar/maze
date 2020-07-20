package map

import items.*

class RoomWithBowl(bonzo: Bonzo) : Room() {
    init {
        desc = "This room only has a bowl in it"
        val bowl = Bowl(this, bonzo)
        addItem(bowl)
    }
}