package map

import game.GameResult
import game.GameResultCode
import game.Player

class Exit : Room("exit") {
    override fun triggerEntranceEvent(moveResult: GameResult): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Congrats! You made it out of the maze!")
    }

    override fun peekResult(player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "You can see the exit! You're almost out!!")
    }
}