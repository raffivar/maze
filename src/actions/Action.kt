package actions

import game.GameResult
import game.Player

abstract class Action(val name: String) {
    abstract fun execute(player: Player, args: List<String>): GameResult
}