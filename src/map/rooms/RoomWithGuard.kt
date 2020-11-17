package map.rooms

import game.GameResult
import game.GameResultCode
import items.Item
import map.rooms.Room

class RoomWithGuard : Room("roomWithGuard") {
    init {
        addItem(Item("Guard", "This guard seems good at catching people"))
    }

    override fun triggerEntranceEvent(moveResult: GameResult): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Caught by a guard!!")
    }
}