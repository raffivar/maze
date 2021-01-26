package map.rooms

import game.GameResult
import game.GameResultCode
import items.Item
import map.rooms.interfaces.HasEnterEvent
import player.Player

class RoomWithGuard : Room("roomWithGuard"), HasEnterEvent {
    init {
        addItem(Item("Guard", "This guard seems good at catching people"))
    }

    override fun onEntered(defaultResult: GameResult, player: Player): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Caught by a guard!!")
    }
}