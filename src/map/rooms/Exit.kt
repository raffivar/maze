package map.rooms

import game.GameResult
import game.GameResultCode
import map.rooms.interfaces.EnterEventRoom
import map.rooms.interfaces.PeekEventRoom
import player.Player

class Exit : Room("exit"), PeekEventRoom, EnterEventRoom {
    override fun onRoomPeeked(defaultResult: () -> GameResult, player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "O-M-G, you can see outside! FREEDOM IS NEAR.")
    }

    override fun onRoomEntered(defaultResult: () -> GameResult, player: Player): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Congrats! You made it out of the maze!")
    }
}