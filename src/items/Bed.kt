package items

import data.BedData
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

    override fun getData() : BedData {
        return BedData(name, wasExaminedBefore)
    }
}