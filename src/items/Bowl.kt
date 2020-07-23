package items

import game.GameResult
import game.GameResultCode
import map.Room

class Bowl(val room: Room, private val modifyRoomWhenExamined: () -> GameResult) : Item("Bowl", "") {
    private var isEmpty = false
    private var firstTimeExamined = true

    override fun examine(): GameResult {
        return if (firstTimeExamined) {
            firstTimeExamined = false
            modifyRoomWhenExamined()
        } else {
            description = if (!isEmpty) {
                "This bowl is full of Bonzo."
            } else {
                "Just a regular empty bowl with Bonzo crumbs in it."
            }
            super.examine()
        }
    }

    fun empty() {
        isEmpty = true
    }
}