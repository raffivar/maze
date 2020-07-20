package actions

import game.GameResult
import game.Player
import game.GameResultCode

class Help(private val actions: List<Action>) : Action("Help", "Help - prints out this menu") {
    fun execute(player: Player): GameResult {
        return execute(player, listOf())
    }

    override fun execute(player: Player, args: List<String>): GameResult {
        var helpMenu = "The operations available to you are:\n"
        for ((i, action) in actions.withIndex()) {
            helpMenu += "${i + 1}. ${action.howToUse}\n"
        }
        return GameResult(GameResultCode.SUCCESS, helpMenu)
    }
}