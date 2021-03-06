package items

import game.GameResult
import game.GameResultCode
import player.Player
import items.interfaces.UsableWithoutTaking

class Rope : Item("Rope", "An old, thick, rope. Looks like it's relatively strong."),
    UsableWithoutTaking {
    private var isInUse = false

    override fun take(player: Player): GameResult {
        if (isInUse) {
            return GameResult(GameResultCode.SUCCESS, "Cannot take [${this.name}], it's clearly in use.")
        }
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }

    override fun useOn(player: Player, itemUsedOn: Item): GameResult {
        val useResult = itemUsedOn.usedBy(player, this)
        if (useResult.gameResultCode == GameResultCode.SUCCESS) {
            player.inventory.removeItem(this)
            player.currentRoom.addItem(this)
            isInUse = true
        }
        return useResult
    }
}