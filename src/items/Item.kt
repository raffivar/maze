package items

import data.SavableItemData
import game.GameResult
import game.GameResultCode
import game.Player

open class Item(var name: String, var description: String) {
    open fun examine(): GameResult {
        return GameResult(GameResultCode.SUCCESS, description)
    }

    open fun take(player: Player): GameResult {
        return GameResult(GameResultCode.FAIL, "[${this.name}] is not something you can take")
    }

    open fun useOn(player: Player, itemUsedOn: Item): GameResult {
        return itemUsedOn.usedBy(player, this)
    }

    open fun usedBy(player: Player, itemUsed: Item): GameResult {
        return GameResult(GameResultCode.FAIL, "[${itemUsed.name}] cannot be used on [${this.name}]")
    }

    open fun getData(): SavableItemData {
        return SavableItemData(name)
    }
}