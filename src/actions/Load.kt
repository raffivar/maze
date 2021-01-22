package actions

import game.*
import player.Player

class Load(private val gameDataManager: GameDataManager) : Action("Load", "Load - load saved game") {
    override fun execute(player: Player, args: List<String>): GameResult {
        val loadResult = gameDataManager.load()
        return when (loadResult.gameResultCode) {
            GameResultCode.SUCCESS -> {
                val examineResult = player.currentRoom.examine()
                GameResult(examineResult.gameResultCode, "${loadResult.message}\n${examineResult.message}")
            }
            else -> loadResult
        }
    }
}