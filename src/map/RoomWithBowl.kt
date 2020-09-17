package map

import data.SavableRoom
import game.GameResult
import game.GameResultCode
import items.*

class RoomWithBowl(private val bonzo: Bonzo) : Room("roomWithBowl"), SavableRoom {
    private val bowl = Bowl(this, this::addBonzoToBowl)

    init {
        baseDescription = "This room only has a bowl in it."
        bonzo.bowl = bowl
        addItem(bowl)
    }

    private fun addBonzoToBowl(): GameResult {
        addItem(bonzo)
        return GameResult(GameResultCode.SUCCESS, "You discover [${bonzo.name}] in the bowl.")
    }

    override fun saveRoomData(mapBuilder: MapBuilder) {
        mapBuilder.items[bowl.name] = bowl
        mapBuilder.items[bonzo.name] = bonzo
    }

}