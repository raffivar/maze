package actions

import game.GameResult
import player.Player
import game.GameResultCode

class Help(private val actions: List<Action>) : Action("Help", "Help - prints out this menu") {
    override fun execute(player: Player, args: List<String>): GameResult {
        var helpMenu = "Available actions:\n"
        for ((i, action) in actions.withIndex()) {
            helpMenu += "${i + 1}. ${action.howToUse}\n"
        }
        helpMenu = helpMenu.substring(0, helpMenu.length - 1)
        return GameResult(GameResultCode.SUCCESS, helpMenu)
    }
}