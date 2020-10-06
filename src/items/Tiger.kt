package items

import data.ItemData
import data.TigerData
import game.*

class Tiger(killingItem: Item) : Item("Tiger", null), SavableItem {
    private val aliveDescription = "This is a really big, fat, lazy [$name]."
    private val deadDescription = "This is a really big, fat, lazy [$name]. It's also dead."
    var isAlive = true
    private val itemsToFunctions = HashMap<Item, (Player, Item) -> GameResult>()

    init {
        itemsToFunctions[killingItem] = this::distract
    }

    override fun examine(): GameResult {
        return when (isAlive) {
            true -> examine(aliveDescription)
            false -> examine(deadDescription)
        }
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player, itemUsed)
    }

    private fun distract(player: Player, itemUsed: Item): GameResult {
        if (!isAlive) {
            return GameResult(
                GameResultCode.FAIL,
                "[${this.name}] is already dead."
            )
        }
        isAlive = false
        return GameResult(
            GameResultCode.SUCCESS,
            "The [${this.name}] eats off your hand and dies. Congrats, asshole, you killed the [${this.name}]."
        )
    }

    override fun getData() : ItemData {
        return TigerData(name, isAlive)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as TigerData
        isAlive = data.isAlive
    }
}