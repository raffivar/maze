package items

import data.DoorData
import data.ItemData
import data.SavableItem
import game.Constraint
import game.GameResult
import game.GameResultCode
import game.Player
import java.util.ArrayList


private const val closedDescription = "An old, closed door."
private const val openDescription = "An old, open door."

class Door(override var isClosed: Boolean = true) : Item("Door", if (isClosed) closedDescription else openDescription), Openable {
    private val constraintsToOpen = ArrayList<Constraint>()

    fun addConstraintToOpen(constraint: Constraint) {
        constraintsToOpen.add(constraint)
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
}