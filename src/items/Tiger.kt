package items

import data.ItemData
import data.TigerData
import game.*

class Tiger : Item("Tiger", null), SavableItem {
    var isAlive = true
    var isPoisoned = false
    var facingSouth = true
    private val aliveDescription = "This is a really big, fat, lazy [$name]."
    private val deadDescription = "This gigantic [$name] seems to be dead."

    override fun examine(): GameResult {
        return when (isAlive) {
            true -> examine(aliveDescription)
            false -> examine(deadDescription)
        }
    }

    fun poison() {
        isPoisoned = true
    }

    fun kill() {
        isAlive = false
    }

    override fun getData() : ItemData {
        return TigerData(name, isAlive, isPoisoned, facingSouth)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as TigerData
        isAlive = data.isAlive
        isPoisoned = data.isPoisoned
        facingSouth = data.facingSouth
    }
}