package items

import game.GameResult
import game.GameResultCode
import map.Room

class Bed(val room: Room, private val modifyRoomWhenExamined: () -> GameResult) : Item("Bed", "This is an old, uncomfortable bed.") {
    var firstTimeExamined = true
    override fun examine(): GameResult {
        return if (firstTimeExamined) {
            firstTimeExamined = false
            modifyRoomWhenExamined()
        } else {
            super.examine()
        }
    }
}