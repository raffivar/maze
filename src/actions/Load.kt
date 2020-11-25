package actions

import game.*
import player.Player

class Load(private val gameDataManager: GameDataManager) : Action("Load", "Load - load saved game") {
    override fun execute(player: Player, args: List<String>): GameResult {
        val loadResult = gameDataManager.load()
        return when (loadResult.gameResultCode) {
            GameResultCode.SUCCESS -> {
                player.currentRoom.examineWithPrefix(loadResult.message)
            }
            else -> loadResult
        }
    }
}