package actions

import game.GameResult
import player.Player
import game.GameResultCode

class Exit : Action("Exit", "Exit - terminate game") {
    override fun execute(player: Player, args: List<String>): GameResult {
        return GameResult(GameResultCode.GAME_OVER, "Game terminated.")
    }
}