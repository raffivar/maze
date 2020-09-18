package items

import data.BedData
import data.SavableItemData
import game.GameResult

class Bed(private val modifyRoomWhenExamined: () -> GameResult) : Item("Bed", "This is an old, uncomfortable bed.") {
    private var wasExaminedBefore = false

    override fun examine(): GameResult {
        return if (wasExaminedBefore) {
            super.examine()
        } else {
            wasExaminedBefore = true
            modifyRoomWhenExamined()
        }
    }

    override fun getData(): SavableItemData {
        return BedData(name, wasExaminedBefore)
    }
}