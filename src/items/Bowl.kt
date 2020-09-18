package items

import data.BowlData
import data.SavableItemData
import game.GameResult
import map.Room

class Bowl(val room: Room, private val modifyRoomWhenExamined: () -> GameResult) : Item("Bowl", "") {
    private var isFull = true
    private var wasExaminedBefore = false

    override fun examine(): GameResult {
        return if (wasExaminedBefore) {
            description = if (isFull) {
                "This bowl is full of Bonzo."
            } else {
                "Just a regular empty bowl with Bonzo crumbs in it."
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

    override fun getData(): SavableItemData {
        return BowlData(name, wasExaminedBefore, isFull)
    }
}