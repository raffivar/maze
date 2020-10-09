package items

import data.BowlData
import data.ItemData
import game.GameResult
import game.GameResultCode
import game.Player
import map.Room

class Bowl(private val poison: Poison) : Item("Bowl", ""), SavableItem {
    var hasPoison = false
    private var wasExaminedBefore = false
    private val itemsToFunctions = HashMap<Item, (Player) -> GameResult>()

    init {
        itemsToFunctions[poison] = this::addPoison
    }

    override fun examine(): GameResult {
        description = if (hasPoison) {
            "This bowl is full of poison."
        } else {
            "Just a regular empty bowl with poison leftovers in it."
        }
        return super.examine()
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player)
    }

    private fun addPoison(player: Player): GameResult {
        if (hasPoison) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] already contains poison.")
        }
        hasPoison = true
        return GameResult(GameResultCode.SUCCESS, "Filled some [${poison.name}] into the [${this.name}].")
    }

    override fun getData(): ItemData {
        return BowlData(name, wasExaminedBefore, hasPoison)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as BowlData
        wasExaminedBefore = data.wasExaminedBefore
        hasPoison = data.containsPoison
    }


}