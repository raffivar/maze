package map.rooms

import game.GameResult
import game.GameResultCode
import map.rooms.interfaces.EnterEventRoom
import player.Player

class Exit : Room("exit", peekDescription = "You can see the exit! You're almost out!!"), EnterEventRoom {
    override fun onRoomEntered(defaultResult: () -> GameResult, player: Player): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Congrats! You made it out of the maze!")
    }
}