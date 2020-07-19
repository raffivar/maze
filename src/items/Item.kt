package items

import game.GameResult
import game.GameResultCode
import game.Player

open class Item(var name: String, var desc: String) {
    open fun examine(): GameResult {
        return GameResult(GameResultCode.SUCCESS, desc)
    }

    open fun useOn(player: Player, itemUsedOn: Item): GameResult {
        return itemUsedOn.usedBy(player, this)
    }

    open fun usedBy(player: Player, itemUsed: Item): GameResult {
        return GameResult(GameResultCode.FAIL, "[${itemUsed.name}] cannot be used on [${this.name}]")
    }
}