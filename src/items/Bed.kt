package items

import data.items.BedData
import data.items.ItemData
import game.GameResult
import game.GameResultCode
import player.Player
import items.interfaces.SavableItem

class Bed(private val modifyRoomWhenExamined: () -> GameResult) : Item("Bed", "An old, extremely uncomfortable bed."),
    SavableItem {
    private var wasExaminedBefore = false

    override fun examine(): GameResult {
        return if (wasExaminedBefore) {
            super.examine()
        } else {
            wasExaminedBefore = true
            modifyRoomWhenExamined()
        }
    }

    override fun getDataToSaveToFile(): ItemData {
        return BedData(name, wasExaminedBefore)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as BedData
        wasExaminedBefore = data.wasExaminedBefore
    }

    override fun take(player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "You're seriously trying to take this?")
    }
}