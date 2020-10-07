package items

import data.LockData
import data.ItemData
import data.RopeDependedItemData
import game.GameResult
import game.GameResultCode
import game.Player

class RopeDependedItem(name: String, private val rope: Rope) : Item(name, null), SavableItem {
    var hasNothingAttached: Boolean = true
    private val beforeRope = "This is just a standard [${this.name}]"
    private val afterRope = "This is just a standard [${this.name}]. There's a rope attached to it."
    private val itemsToFunctions = HashMap<Item, (Player) -> GameResult>()

    init {
        itemsToFunctions[rope] = this::tieRope
    }

    override fun examine(): GameResult {
        return when (hasNothingAttached) {
            true -> examine(beforeRope)
            false -> examine(afterRope)
        }
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player)
    }

    private fun tieRope(player: Player): GameResult {
        if (!hasNothingAttached) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] already has rope attached.")
        }
        hasNothingAttached = false
        return GameResult(GameResultCode.SUCCESS, "You attached the [${rope.name}] to the [${this.name}].")
    }

    override fun getData() : ItemData {
        return RopeDependedItemData(name, hasNothingAttached)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as RopeDependedItemData
        hasNothingAttached = data.hasRopeAttached
    }
}