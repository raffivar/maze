package actions

import GameResult
import Player

abstract class Action(val name: String, var args: List<String>) {
    abstract fun execute(player: Player): GameResult
}