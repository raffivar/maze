package items

import game.GameResult
import game.Player

interface Openable {
    var isClosed: Boolean
    fun open(player: Player): GameResult
}