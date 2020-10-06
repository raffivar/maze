package items

import data.ItemData
import data.TigerData
import game.*

class Tiger() : Item("Tiger", "This is a really big, fat, lazy tiger"), SavableItem {

    private val movingDescription = "This dog seems to be moving a lot."
    private val notMovingDescription = "This dog seems to be busy with food."
    var isAlive = true
    private val itemsToFunctions = HashMap<Item, (Player, Item) -> GameResult>()

    override fun examine(): GameResult {
        return when (isAlive) {
            true -> examine(movingDescription)
            false -> examine(notMovingDescription)
        }
    }

    fun setStoppingItem(itemUsed: Item) {
        itemsToFunctions[itemUsed] = this::distract
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
            "The [${this.name}] east off your hand and died. Congrats, asshole, you killed the [${this.name}]."
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