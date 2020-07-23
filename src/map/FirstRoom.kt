package map

import game.Constraint
import game.GameResult
import game.GameResultCode

import items.*

class FirstRoom : Room() {
    private var firstTimeRoomExamined = true
    private val defaultRoomDescription = "This room only contains a bed and a door with a lock on it."
    private val key = Key(this::keyTaken)
    private val bed = Bed(this, this::bedExamined)

    init {
        val door = Door()
        addItem(door)
        addConstraint(Direction.WEST, Constraint(door::isClosed, "The [${door.name}] is closed."))
        val lock = Lock()
        addItem(lock)
        addItem(bed)
        lock.setUnlockingItem(key)
        door.addConstraintToOpen(Constraint(lock::isLocked, "Looks like you have to to do something about the [${lock.name}] first."))
    }

    override fun examine(): GameResult {
        baseDescription = if (firstTimeRoomExamined) {
            firstTimeRoomExamined = false
            "You wake up in small room. There's only the bed you woke up on (which is horribly uncomfortable) and a door with a lock on it."
        } else {
            defaultRoomDescription
        }
        return super.examine()
    }

    private fun bedExamined(): GameResult {
        firstTimeRoomExamined = false
        addItem(key)
        baseDescription = "This room has a closed door with a lock on it, but after examining the bed, you also notice a small key under it."
        return GameResult(GameResultCode.SUCCESS, "${bed.desc} - You discover a small [${key.name}] under it.")
    }

    private fun keyTaken() {
        baseDescription = defaultRoomDescription
    }
}