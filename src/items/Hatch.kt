package items

import data.HatchData
import data.ItemData
import data.TigerData
import game.*

class Hatch(private val ladder: Ladder) : Item("Hatch", "A small hatch. Seems like a human can fit in it."), SavableItem {
    var isTooHigh = true
    private val itemsToFunctions = HashMap<Item, (Player, Item) -> GameResult>()

    init {
        itemsToFunctions[ladder] = this::makeAccessible
    }

    override fun take(player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "... You can't be serious.")
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player, itemUsed)
    }

    private fun makeAccessible(player: Player, itemUsed: Item): GameResult {
        isTooHigh = false
        return GameResult(GameResultCode.SUCCESS, "Placed [${ladder.name}] beneath [${this.name}], making it now accessible.")
    }

    override fun getData(): ItemData {
        return HatchData(name, isTooHigh)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as HatchData
        isTooHigh = data.isAccessible
    }
}