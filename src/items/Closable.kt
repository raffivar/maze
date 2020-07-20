package items

import game.GameResult
import game.Player

interface Closable {
    var isClosed: Boolean
    fun open(player: Player): GameResult
}