package items

import data.DoorData
import data.ItemData
import game.Constraint
import game.GameResult
import game.GameResultCode
import game.Player
import java.util.ArrayList

class Door(override var isClosed: Boolean = true) : Item("Door", null), Openable, SavableItem {
    private val closedDescription = "An old, closed door."
    private val openDescription = "An old, open door."
    private val constraintsToOpen = ArrayList<Constraint>()

    fun addConstraintToOpen(constraint: Constraint) {
        constraintsToOpen.add(constraint)
    }

    override fun examine(): GameResult {
        return when (isClosed) {
            true -> examine(closedDescription)
            false -> examine(openDescription)
        }
    }

    override fun open(player: Player): GameResult {
        for (constraint in constraintsToOpen) {
            if (constraint.isConstraining.invoke()) {
                return GameResult(GameResultCode.FAIL, "You failed to open [${name}] - ${constraint.message}")
            }
        }
        isClosed = false
        description = openDescription
        return GameResult(GameResultCode.SUCCESS, "The [${this.name}] slowly cracks open. It is heavy, but eventually you manage to open it completely.")
    }

    override fun getData() : ItemData {
        return DoorData(name, isClosed)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as DoorData
        isClosed = data.isClosed
    }
}

