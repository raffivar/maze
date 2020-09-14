package actions

import game.GameDataManager
import game.GameResult
import game.GameResultCode
import game.Player

class Load(private val gameDataManager: GameDataManager) : Action("Load", "Load - load saved game") {
    override fun execute(player: Player, args: List<String>): GameResult {
        return gameDataManager.load()
    }
}