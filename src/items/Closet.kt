package items

import data.items.ClosetData
import data.items.ItemData
import game.GameResult
import game.GameResultCode
import player.Player
import items.interfaces.Openable
import items.interfaces.SavableItem

class Closet(override var isOpen: Boolean = false, private val modifyRoomWhenExamined: () -> GameResult) : Item("Closet", null),
    Openable, SavableItem {
    private val closedDescription = "A standard, boring [${name}]. It is closed."
    private val openDescription = "A standard, boring [${name}]. It is open."

    override fun examine(): GameResult {
        return when (isOpen) {
            true -> examine(openDescription)
            false -> examine(closedDescription)
        }
    }

    override fun open(player: Player): GameResult {
        isOpen = true
        description = openDescription
        return modifyRoomWhenExamined.invoke()
    }

    override fun getDataToSaveToFile() : ItemData {
        return ClosetData(name, isOpen)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as ClosetData
        isOpen = data.isOpen
    }

    override fun take(player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "You're strong, but you're not *that* strong.")
    }
}

