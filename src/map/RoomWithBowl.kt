package map

import items.*

class RoomWithBowl(bonzo: Bonzo) : Room() {
    init {
        baseDescription = "This room only has a bowl in it"
        val bowl = Bowl(this, bonzo)
        addItem(bowl)
    }
}