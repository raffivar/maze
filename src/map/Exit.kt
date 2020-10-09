package map

import game.GameResult
import game.GameResultCode

class Exit : Room("exit") {
    override fun triggerEntranceEvent(): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Congrats! You made it out of the maze!")
    }

    override fun peekResult(): GameResult {
        return GameResult(GameResultCode.SUCCESS, "You can see the exit! You're almost out!!")
    }
}