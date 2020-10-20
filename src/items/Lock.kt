package items

import data.LockData
import data.ItemData
import game.GameResult
import game.GameResultCode
import game.Player

class Lock(itemToUnlock: Item, var isLocked: Boolean = true) : Item("Lock", null), SavableItem {
    private val lockedDescription = "This lock is old and rusty. It's also locked."
    private val unlockedDescription = "It's unlocked, so you don't really care about it. It's also rusty, so you don't really want to touch it *AGAIN*."
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
            return GameResult(GameResultCode.FAIL, "The [${this.name}] already unlocked. Also, why would you want to touch it again, with all that disgusting rust??")
        }
        isLocked = false
        return GameResult(GameResultCode.SUCCESS, "The [${this.name}] is extremely rusty,\nso it's a bit difficult,\nnot to mention, you feel the rust on your hands (so ew, ew, EWWWWW),\nbut after a few tries, you eventually manage to unlock it.")
    }

    override fun getData() : ItemData {
        return LockData(name, isLocked)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as LockData
        isLocked = data.isLocked
    }
}