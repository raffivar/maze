package items

import game.GameResult
import game.GameResultCode
import player.Player

class Poison : Item("Poison", "This (edible) poison looks like it can... poison... someone.") {
    override fun take(player: Player): GameResult {
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}].")
    }

    override fun useOn(player: Player, itemUsedOn: Item): GameResult {
        val useResult = itemUsedOn.usedBy(player, this)
        if (useResult.gameResultCode == GameResultCode.SUCCESS) {
            player.inventory.removeItem(this)
        }
        return useResult
    }
}