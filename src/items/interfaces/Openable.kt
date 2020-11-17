package items.interfaces

import game.GameResult
import player.Player

interface Openable {
    var isOpen: Boolean
    fun open(player: Player): GameResult
}