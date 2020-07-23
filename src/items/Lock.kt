package items

import game.GameResult
import game.GameResultCode
import game.Player


private const val lockedDescription = "This dusty old lock seems to be locked."
private const val unlockedDescription = "A dusty, unlocked lock."

class Lock(var isLocked: Boolean = true) : Item("Lock", if (isLocked) lockedDescription else unlockedDescription) {
    private val itemsToFunctions = HashMap<Item, (Player, Item) -> GameResult>()

    fun setItemToUnlock(itemUsed: Item) {
        itemsToFunctions[itemUsed] = this::unlock
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player, itemUsed)
    }

    private fun unlock(player: Player, itemUsed: Item): GameResult {
        if (!isLocked) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] already unlocked.")
        }
        isLocked = false
        desc = unlockedDescription
        return GameResult(GameResultCode.SUCCESS, "Unlocked [${this.name}].")
    }
}