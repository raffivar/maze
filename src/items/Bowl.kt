package items

import data.items.BowlData
import data.items.ItemData
import game.GameResult
import game.GameResultCode
import player.Player
import items.interfaces.SavableItem

class Bowl(private val poison: Poison) : Item("Bowl", ""), SavableItem {
    enum class BowlStatus { PRE_EATEN, POISONED, POST_EATEN }

    var status = BowlStatus.PRE_EATEN
    private var wasExaminedBefore = false
    private val itemsToFunctions = HashMap<Item, (Player) -> GameResult>()

    init {
        itemsToFunctions[poison] = this::addPoison
    }

    override fun examine(): GameResult {
        description = when (status) {
            BowlStatus.PRE_EATEN -> "This bowl is full with tiger food. Whatever that means."
            BowlStatus.POISONED -> "If only people knew that this food is poisoned..."
            BowlStatus.POST_EATEN -> "An almost empty bowls, just a few scraps of tiger food and (edible) poison in it."
        }

        return super.examine()
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player)
    }

    private fun addPoison(player: Player): GameResult {
        return when (status) {
            BowlStatus.PRE_EATEN -> {
                status = BowlStatus.POISONED
                GameResult(GameResultCode.SUCCESS, "Filled some [${poison.name}] into the [${this.name}].")
            }
            BowlStatus.POISONED -> GameResult(GameResultCode.FAIL, "[${this.name}] already contains poison.")
            BowlStatus.POST_EATEN -> GameResult(GameResultCode.FAIL, "[${this.name}] is empty, there's nothing to hide the poison in between.")
        }
    }

    fun setFoodEaten() {
        status = BowlStatus.POST_EATEN
    }

    override fun getData(): ItemData {
        return BowlData(name, wasExaminedBefore, status)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as BowlData
        wasExaminedBefore = data.wasExaminedBefore
        status = data.status
    }


}