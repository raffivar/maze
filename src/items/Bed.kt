package items

import data.BedData
import data.ItemData
import data.SavableObject
import game.GameData
import game.GameResult

class Bed(private val modifyRoomWhenExamined: () -> GameResult) : Item("Bed", "This is an old, uncomfortable bed."), SavableObject {
    private var wasExamined = false
    set (value) {
        if (!value) {
            modifyRoomWhenExamined()
        }
        field = value
    }

    override fun examine(): GameResult {
        return if (wasExamined) {
            super.examine()
        } else {
            wasExamined = true
            modifyRoomWhenExamined()
        }
    }

    override fun getData() : ItemData {
        return BedData(name, description, wasExamined)
    }

    override fun saveIntoGameData(gameData: GameData) {
        TODO("Not yet implemented")
    }
}