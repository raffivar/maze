package items

import game.GameResult
import game.GameResultCode
import game.Player

class Lock(override var isLocked: Boolean = true) : Item("Lock", "This is a lock"), Lockable {
    private val itemsToFunctions = HashMap<Item, (Player, Item) -> GameResult>()

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player, itemUsed)
    }

    fun setItemToUnlock(itemUsed: Item) {
        itemsToFunctions[itemUsed] = this::unlock
    }

    override fun unlock(player: Player, itemUsed: Item): GameResult {
        if (!isLocked) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] already unlocked")
        }
        isLocked = false
        //player.inventory.remove(itemUsed.name)
        return GameResult(GameResultCode.SUCCESS, "Unlocked [${this.name}]")
    }
}