package items

import data.DoorData
import data.ItemData
import game.Constraint
import game.GameResult
import game.GameResultCode
import game.Player
import java.util.ArrayList

class Closet(override var isClosed: Boolean = true, private val modifyRoomWhenExamined: () -> GameResult) : Item("Closet", null), Openable, SavableItem {
    private val closedDescription = "This [${name}] is closed."
    private val openDescription = "This [${name}] is open."
    private val constraintsToOpen = ArrayList<Constraint>()

    override fun examine(): GameResult {
        return when (isClosed) {
            true -> examine(closedDescription)
            false -> examine(openDescription)
        }
    }

    override fun open(player: Player): GameResult {
        for (constraint in constraintsToOpen) {
            if (constraint.isConstraining.invoke()) {
                return GameResult(GameResultCode.FAIL, "You failed to open the [${name}] - ${constraint.message}.")
            }
        }
        isClosed = false
        description = openDescription
        return modifyRoomWhenExamined.invoke()
    }

    override fun getData() : ItemData {
        return DoorData(name, isClosed)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as DoorData
        isClosed = data.isClosed
    }
}

