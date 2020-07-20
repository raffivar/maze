package items

import game.Constraint
import game.GameResult
import game.GameResultCode
import game.Player
import java.util.ArrayList

class Door(override var isClosed: Boolean = true) : Item("Door", "This is a door"), Closable {
    private val constraintsToOpen = ArrayList<Constraint>()

    override fun open(player: Player): GameResult {
        for (constraint in constraintsToOpen) {
            if (constraint.isConstraining.invoke()) {
                return GameResult(GameResultCode.FAIL, "You failed to open [${name}] - ${constraint.message}")
            }
        }
        isClosed = false
        return GameResult(GameResultCode.SUCCESS, "Opened [${name}]")
    }

    fun addConstraintToOpen(constraint: Constraint) {
        constraintsToOpen.add(constraint)
    }
}