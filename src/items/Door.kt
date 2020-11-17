package items

import data.items.DoorData
import data.items.ItemData
import actions.constraints.Constraint
import game.GameResult
import game.GameResultCode
import player.Player
import items.interfaces.Openable
import items.interfaces.SavableItem
import java.util.ArrayList

class Door(val doorId: String, override var isOpen: Boolean = true) : Item(doorId, null),
    Openable, SavableItem {
    private val closedDescription = "An old wooden door, with little lines carved on it. Seems like someone has been marking their jail time (The door is closed)."
    private val openDescription = "Love is an... <Insert Disney Line here>."
    private val constraintsToOpen = ArrayList<Constraint>()

    fun addConstraintToOpen(constraint: Constraint) {
        constraintsToOpen.add(constraint)
    }

    override fun examine(): GameResult {
        return when (isOpen) {
            true -> examine(openDescription)
            false -> examine(closedDescription)
        }
    }

    override fun open(player: Player): GameResult {
        for (constraint in constraintsToOpen) {
            if (constraint.isConstraining.invoke()) {
                return GameResult(GameResultCode.FAIL, "You failed to open [${name}] - ${constraint.message}")
            }
        }
        isOpen = true
        description = openDescription
        return GameResult(
            GameResultCode.SUCCESS,
            "The [${this.name}] slowly cracks open. It is heavy, but eventually you manage to open it completely."
        )
    }

    override fun getData(): ItemData {
        return DoorData(name, isOpen)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as DoorData
        isOpen = data.isOpen
    }
}

