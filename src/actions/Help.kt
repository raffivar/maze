package actions

import game.GameResult
import game.Player
import game.GameResultCode

class Help : Action("Help") {
    override fun execute(player: Player, args: List<String>): GameResult {
        var helpMenu = "The operations available to you are:\n"
        helpMenu += "GO [DIRECTION]\n"
        helpMenu += "EXAMINE [room/item in room]\n"
        helpMenu += "TAKE [item in room]\n"
        helpMenu += "HELP - prints this menu\n"
        return GameResult(GameResultCode.OK, helpMenu)
    }
}