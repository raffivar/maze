package actions

import game.GameResult
import game.Player

abstract class Action(val name: String, var args: List<String>) {
    abstract fun execute(player: Player): GameResult
}