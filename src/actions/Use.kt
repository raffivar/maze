package actions

import game.GameResult
import game.GameResultCode
import game.Player

class Use : Action("Use", "Use [item1] on [item2]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty() || args.size < 3 || !args[1].equals("on", true)) {
            return GameResult(GameResultCode.ERROR, "Invalid arguments. Please use the format: '$howToUse'")
        }
        val item1 = args[0]
        val item2 = args[2]
        return player.use(item1, item2)
    }
}