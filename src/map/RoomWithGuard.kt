package map

import game.GameResult
import game.GameResultCode
import items.Item

class RoomWithGuard : Room("roomWithGuard") {
    init {
        addItem(Item("Guard", "This guard seems good at catching people"))
    }

    override fun triggerEntranceEvent(): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Caught by a guard!!")
    }
}