package items

import game.GameResult
import game.Player

class Key : Item("Key", "This is a key"), Takable {
    override fun useOn(player: Player, itemToUseOn: Item): GameResult {
        return itemToUseOn.usedBy(player, this)
    }
}