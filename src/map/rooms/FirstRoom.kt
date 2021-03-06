package map.rooms

import actions.constraints.Constraint
import game.GameResult
import game.GameResultCode
import items.*
import items.maps.ItemMap
import map.directions.Direction

class FirstRoom(private val door: Door) : Room("firstRoom", "This room's only furniture is an extremely uncomfortable bed.") {
    private var bed: Bed
    private var key: Key
    private var lock: Lock
    private var mirror: Mirror
    private var shard: Shard
    private var brokenMirror: BrokenMirror

    init {
        bed = Bed(this::bedExamined)
        addItem(bed)
        key = Key()
        lock = Lock(key)
        addItem(lock)
        mirror = Mirror(this::mirrorBroken)
        addItem(mirror)
        shard = Shard()
        brokenMirror = BrokenMirror()
        addItem(door)
        addConstraint(Direction.WEST,
            Constraint({ !door.isOpen }, "The [${door.name}] is closed.")
        )
        door.addConstraintToOpen(
            Constraint(lock::isLocked,
                "Looks like you have to to do something about the [${lock.name}] first."
            )
        )
    }

    private fun bedExamined(): GameResult {
        addItem(key)
        return GameResult(GameResultCode.SUCCESS, "You're curious as to why this bed is so excruciatingly uncomfortable.\nYou thoroughly examine it from all angles and discover a small [${key.name}] under it.")
    }

    private fun mirrorBroken(): GameResult {
        addItem(shard)
        removeItem(mirror)
        addItem(brokenMirror)
        val message = "You broke the [${mirror.name}]. You monster. Look what you did to the floor of this room!!"
        return GameResult(GameResultCode.SUCCESS, message)
    }

    override fun saveDataToDB(gameItems: ItemMap) {
        super.saveDataToDB(gameItems)
        gameItems.addItem(key)
        gameItems.addItem(brokenMirror)
        gameItems.addItem(shard)
    }
}