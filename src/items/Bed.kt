package items

import data.BedData
import data.ItemData
import game.GameResult
import game.GameResultCode
import game.Player

class Bed(private val modifyRoomWhenExamined: () -> GameResult) : Item("Bed", "This is an old, uncomfortable bed."), SavableItem {
    private var wasExaminedBefore = false

    override fun examine(): GameResult {
        return if (wasExaminedBefore) {
            super.examine()
        } else {
            wasExaminedBefore = true
            modifyRoomWhenExamined()
        }
    }

    override fun getData(): ItemData {
        return BedData(name, wasExaminedBefore)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as BedData
        wasExaminedBefore = data.wasExaminedBefore
    }

    override fun take(player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "You're seriously try to take this?")
    }
}