package map.rooms

import game.GameResult
import game.GameResultCode
import items.Item
import map.rooms.Room
import map.rooms.interfaces.EnterEventRoom
import player.Player

class RoomWithGuard : Room("roomWithGuard"), EnterEventRoom {
    init {
        addItem(Item("Guard", "This guard seems good at catching people"))
    }

    override fun onRoomEntered(defaultResult: GameResult, player: Player): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Caught by a guard!!")
    }
}