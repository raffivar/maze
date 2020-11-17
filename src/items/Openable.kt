package items

import game.GameResult
import game.Player

interface Openable {
    var isOpen: Boolean
    fun open(player: Player): GameResult
}