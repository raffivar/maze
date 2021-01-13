package map.rooms

import game.GameResult
import game.GameResultCode

class Exit : Room("exit", peekDescription = "You can see the exit! You're almost out!!") {
    override fun triggerEntranceEvent(moveResult: GameResult): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Congrats! You made it out of the maze!")
    }
}