package items

import game.GameResult
import game.GameResultCode
import game.Player

open class Item(var name: String, var desc: String) {
    open fun examine(): GameResult {
        return GameResult(GameResultCode.SUCCESS, desc)
    }

    open fun useOn(player: Player, itemToUseOn: Item): GameResult {
        return usedBy(player, this)
    }

    open fun usedBy(player: Player, itemUsed: Item): GameResult {
        return GameResult(GameResultCode.FAIL, "[${itemUsed.name}] cannot be used on [${this.name}]")
    }
}