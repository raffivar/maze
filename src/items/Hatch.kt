package items

import data.HatchData
import data.ItemData
import data.TigerData
import game.*

class Hatch(private val ladder: Ladder) : Item("Hatch", "A small hatch in the ceiling"), SavableItem {
    var isAccessible = false
    private val itemsToFunctions = HashMap<Item, (Player, Item) -> GameResult>()

    init {
        itemsToFunctions[ladder] = this::makeAccessible
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player, itemUsed)
    }

    private fun makeAccessible(player: Player, itemUsed: Item): GameResult {
        isAccessible = true
        ladder.markAsUsed()
        player.currentRoom.addItem(ladder)
        return GameResult(GameResultCode.SUCCESS, "The [${this.name}] is now accessible.")
    }

    override fun getData(): ItemData {
        return TigerData(name, isAccessible)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as HatchData
        isAccessible = data.isAccessible
    }
}