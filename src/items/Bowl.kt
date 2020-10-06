package items

import data.BowlData
import data.ItemData
import game.GameResult
import map.Room

class Bowl(val room: Room, private val modifyRoomWhenExamined: () -> GameResult) : Item("Bowl", ""), SavableItem {
    private var isFull = true
    private var wasExaminedBefore = false

    override fun examine(): GameResult {
        return if (wasExaminedBefore) {
            description = if (isFull) {
                "This bowl is full of poison."
            } else {
                "Just a regular empty bowl with poison leftovers in it."
            }
            super.examine()
        } else {
            wasExaminedBefore = true
            modifyRoomWhenExamined()
        }
    }

    fun empty() {
        isFull = false
    }

    override fun getData(): ItemData {
        return BowlData(name, wasExaminedBefore, isFull)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as BowlData
        wasExaminedBefore = data.wasExaminedBefore
        isFull = data.isFull
    }
}