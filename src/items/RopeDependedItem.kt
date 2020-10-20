package items

import data.ItemData
import data.RopeDependedItemData
import game.GameResult
import game.Player

open class RopeDependedItem(name: String) : Item(name, null), SavableItem {
    var hasNothingAttached: Boolean = true
    private val beforeRope = "This is just a standard [${this.name}]"
    private val afterRope = "This is just a standard [${this.name}]. There's a rope attached to it."
    internal val itemsToFunctions = HashMap<Item, (Player) -> GameResult>()


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

    override fun getData() : ItemData {
        return RopeDependedItemData(name, hasNothingAttached)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as RopeDependedItemData
        hasNothingAttached = data.hasNothingAttached
    }
}