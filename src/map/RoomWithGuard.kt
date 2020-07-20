package map

import game.GameResult
import game.GameResultCode

class RoomWithGuard : Room() {
    override fun getDescription(): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Caught by a guard!!")
    }
}