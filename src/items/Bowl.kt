package items

import data.BedData
import data.BowlData
import data.ItemData
import game.GameResult
import map.Room

class Bowl(val room: Room, private val modifyRoomWhenExamined: () -> GameResult) : Item("Bowl", "") {
    private var isEmpty = false
    private var wasExamined = false

    override fun examine(): GameResult {
        return if (wasExamined) {
            description = if (!isEmpty) {
                "This bowl is full of Bonzo."
            } else {
                "Just a regular empty bowl with Bonzo crumbs in it."
            }
            super.examine()
        } else {
            wasExamined = true
            modifyRoomWhenExamined()

        }
    }

    fun empty() {
        isEmpty = true
    }

    override fun getData() : ItemData {
        return BowlData(name, description, wasExamined, isEmpty)
    }
}