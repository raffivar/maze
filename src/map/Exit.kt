package map

import game.GameResult
import game.GameResultCode

class Exit : Room() {
    override fun getDescription(): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Congrats! You made it out of the maze!")
    }
}