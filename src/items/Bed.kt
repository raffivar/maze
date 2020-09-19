package items

import data.BedData
import data.ItemData
import game.GameResult

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
}