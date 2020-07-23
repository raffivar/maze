package items

import game.GameResult
import game.GameResultCode
import game.Player

class Bonzo : Item("Bonzo", "Can be used on dogs") {
    override fun take(player: Player): GameResult {
        player.inventory[this.name] = this
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${this.name}]")
    }

    override fun useOn(player: Player, itemUsedOn: Item): GameResult {
        val useResult = itemUsedOn.usedBy(player, this)
        if (useResult.gameResultCode == GameResultCode.SUCCESS) {
            player.inventory.remove(this.name)
        }
        return useResult
    }
}