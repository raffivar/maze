package items

import data.LockData
import data.SavableItemData
import game.GameResult
import game.GameResultCode
import game.Player

class Lock(itemToUnlock: Item, var isLocked: Boolean = true) : Item("Lock", null), SavableItem {
    private val lockedDescription = "This dusty old lock seems to be locked."
    private val unlockedDescription = "A dusty, unlocked lock."
    private val itemsToFunctions = HashMap<Item, (Player) -> GameResult>()

    init {
        itemsToFunctions[itemToUnlock] = this::unlock
    }

    override fun examine(): GameResult {
        return when (isLocked) {
            true -> examine(lockedDescription)
            false -> examine(unlockedDescription)
        }
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player)
    }

    private fun unlock(player: Player): GameResult {
        if (!isLocked) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] already unlocked.")
        }
        isLocked = false
        return GameResult(GameResultCode.SUCCESS, "Unlocked [${this.name}].")
    }

    override fun getData() : SavableItemData {
        return LockData(name, isLocked)
    }

    override fun loadItem(itemData: SavableItemData) {
        val data = itemData as LockData
        isLocked = data.isLocked
    }
}