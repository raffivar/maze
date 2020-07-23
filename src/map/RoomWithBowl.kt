package map

import game.GameResult
import game.GameResultCode
import items.*

class RoomWithBowl(private val bonzo: Bonzo) : Room() {
    init {
        baseDescription = "This room only has a bowl in it."
        val bowl = Bowl(this, this::addBonzoToBowl)
        bonzo.bowl = bowl
        addItem(bowl)
    }

    private fun addBonzoToBowl(): GameResult {
        addItem(bonzo)
        return GameResult(GameResultCode.SUCCESS, "You discover [${bonzo.name}] in the bowl.")
    }

}