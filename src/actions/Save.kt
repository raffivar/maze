package actions

import game.GameDataManager
import game.GameResult
import game.GameResultCode
import game.Player

class Save(private val gameDataManager: GameDataManager) : Action("Save", "Save - save game") {
    override fun execute(player: Player, args: List<String>): GameResult {
        return gameDataManager.save()
    }
}

