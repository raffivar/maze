package actions

import game.GameResult
import player.Player

abstract class Action(val name: String, val howToUse: String) {
    abstract fun execute(player: Player, args: List<String>): GameResult
}