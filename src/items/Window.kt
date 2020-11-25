package items

import data.items.ItemData
import data.items.WindowData
import game.GameResult
import game.GameResultCode
import player.Player
import items.interfaces.Openable

class Window(name: String, private val pole: Pole, private val rope: Rope = pole.rope, override var isOpen: Boolean = false) : RopeDependedItem(name),
    Openable {
    private val exitInfo = "You might be able to climb [DOWN] through."
    private val closedDescription = "This [${name}] is closed. $exitInfo"
    private val openDescription = "This [${name}] is open. $exitInfo"

    init {
        itemsToFunctions[rope] = this::throwRope
    }

    override fun examine(): GameResult {
        return when (isOpen) {
            true -> examine(openDescription)
            false -> examine(closedDescription)
        }
    }

    override fun open(player: Player): GameResult {
        isOpen = true
        description = openDescription
        return GameResult(GameResultCode.FAIL, "Opened [${this.name}].")
    }

    override fun usedBy(player: Player, itemUsed: Item): GameResult {
        val funToRun = itemsToFunctions[itemUsed] ?: return super.usedBy(player, itemUsed)
        return funToRun.invoke(player)
    }

    override fun getDataToSaveToFile() : WindowData {
        return WindowData(name, hasNothingAttached, isOpen)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as WindowData
        hasNothingAttached = data.hasNothingAttached
        isOpen = data.isClosed
    }

    private fun throwRope(player: Player): GameResult {
        if (!hasNothingAttached) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] already has rope thrown out of it.")
        }
        if (pole.hasNothingAttached) {
            return GameResult(GameResultCode.SUCCESS, "You might want to attach the [${rope.name}] to the [${pole.name}] first.")
        }
        if (!isOpen) {
            return GameResult(GameResultCode.FAIL, "Still can't do that if the [${this.name}] is closed.")
        }
        hasNothingAttached = false
        return GameResult(GameResultCode.SUCCESS, "You threw the [${rope.name}] out the [${this.name}].")
    }
}