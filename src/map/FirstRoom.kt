package map

import game.Constraint

import items.*

class FirstRoom : Room() {
    init {
        baseDescription = "You wake up in small room. There's only the bed, and a door with a lock on it."

        val door = Door()
        addItem(door)
        addConstraint(Direction.WEST, Constraint(door::isClosed, "The door is closed!"))

        val lock = Lock()
        addItem(lock)

        val key = Key()
        val bed = Bed(this, key)
        addItem(bed)

        lock.setItemToUnlock(key)
        door.addConstraintToOpen(Constraint(lock::isLocked, "Seems like you have to unlock the lock first."))
    }
}