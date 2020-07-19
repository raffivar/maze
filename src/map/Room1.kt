package map

import game.Constraint

import items.*

class Room1 : Room() {
    init {
        desc = "This room is almost empty."
        addItem(Item("Bed", "This is a bed"))

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