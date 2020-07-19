package items

import game.GameResult
import game.Player

interface Lockable {
    var isLocked: Boolean
    fun unlock(player: Player, itemUsed: Item): GameResult
}