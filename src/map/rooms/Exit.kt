package map.rooms

import game.GameResult
import game.GameResultCode
import map.rooms.interfaces.HasEnterEvent
import map.rooms.interfaces.HasPeekEvent
import player.Player

class Exit : Room("exit"), HasPeekEvent, HasEnterEvent {
    override fun onRoomPeeked(defaultResult: () -> GameResult, player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "O-M-G, you can see outside! FREEDOM IS NEAR.")
    }

    override fun onEntered(defaultResult: GameResult, player: Player): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Congrats! You made it out of the maze!")
    }
}