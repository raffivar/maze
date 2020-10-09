package items

import data.ItemData
import game.GameResult
import game.GameResultCode
import game.Player

open class Item(var name: String, var description: String?) {
    open fun examine(): GameResult {
        return examine(description)
    }

    open fun examine(description: String?): GameResult {
        return GameResult(GameResultCode.SUCCESS, description ?: "This is a mysterious item that has no description.")
    }

    open fun take(player: Player): GameResult {
        return GameResult(GameResultCode.FAIL, "You try to take the [${this.name}]. You fail miserably.")
    }

    open fun drop(player: Player): GameResult {
        player.inventory.removeItem(this)
        player.currentRoom.addItem(this)
        return GameResult(GameResultCode.SUCCESS, "Dropped [${this.name}].")
    }

    open fun breakItem(player: Player): GameResult {
        return GameResult(GameResultCode.FAIL, "You try to break the [${this.name}]. You fail miserably.")
    }

    open fun useOn(player: Player, itemUsedOn: Item): GameResult {
        return itemUsedOn.usedBy(player, this)
    }

    open fun usedBy(player: Player, itemUsed: Item): GameResult {
        return GameResult(GameResultCode.FAIL, "[${itemUsed.name}] cannot be used on [${this.name}]")
    }

    open fun getData(): ItemData {
        return ItemData(name)
    }

    override fun toString(): String {
        return name
    }
}