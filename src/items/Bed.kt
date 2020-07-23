package items

import game.GameResult
import map.Room

class Bed(val room: Room, private val modifyRoomWhenExamined: () -> GameResult) : Item("Bed", "This is an old, uncomfortable bed.") {
    private var firstTimeExamined = true
    override fun examine(): GameResult {
        return if (firstTimeExamined) {
            firstTimeExamined = false
            modifyRoomWhenExamined()
        } else {
            super.examine()
        }
    }
}