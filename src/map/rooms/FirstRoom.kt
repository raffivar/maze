package map.rooms

import data.rooms.FirstRoomData
import data.rooms.RoomData
import data.serializables.SerializableItemData
import data.serializables.SerializableRoomData
import actions.constraints.Constraint
import game.GameResult
import game.GameResultCode
import player.Player

import items.*
import items.maps.ItemMap
import map.directions.Direction
import java.util.ArrayList

class FirstRoom(private val door: Door) : Room("firstRoom") {
    private var bed: Bed
    private var key: Key
    private var lock: Lock
    private var mirror: Mirror
    private var brokenMirror: BrokenMirror
    private var shard: Shard
    private var wasExaminedBefore = false
    private val firstTimeDescription = "You wake up in small room. There's only the bed you woke up on (which is horribly uncomfortable) and a door with a lock on it."
    private val defaultRoomDescription = "This room's only furniture is an extremely uncomfortable bed."

    init {
        bed = Bed(this::bedExamined)
        addItem(bed)
        key = Key(this::keyTaken)
        lock = Lock(key)
        addItem(lock)
        mirror = Mirror(this::mirrorBroken)
        addItem(mirror)
        brokenMirror = BrokenMirror()
        shard = Shard(this::shardTaken)
        addItem(door)
        addConstraint(Direction.WEST,
            Constraint({ !door.isOpen }, "The [${door.name}] is closed.")
        )
        door.addConstraintToOpen(
            Constraint(
                lock::isLocked,
                "Looks like you have to to do something about the [${lock.name}] first."
            )
        )
    }

    override fun examine(): GameResult {
        return when (wasExaminedBefore) {
            false -> {
                wasExaminedBefore = !wasExaminedBefore
                baseDescription = defaultRoomDescription
                super.examineWithAlternativeDescription(firstTimeDescription)
            }
            true -> {
                super.examine()
            }
        }
    }

    private fun bedExamined(): GameResult {
        wasExaminedBefore = true
        addItem(key)
        baseDescription = defaultRoomDescription
        return GameResult(GameResultCode.SUCCESS, "You're curious as to why this bed is so excruciatingly uncomfortable.\nYou thoroughly examine it from all angles and discover a small [${key.name}] under it.")
    }

    private fun keyTaken() {
        baseDescription = defaultRoomDescription
    }

    private fun mirrorBroken(): GameResult {
        wasExaminedBefore = true
        addItem(shard)
        baseDescription = defaultRoomDescription
        removeItem(mirror)
        addItem(brokenMirror)
        val message = "You broke the [${mirror.name}]. You monster. Look what you did to the floor of this room!!"
        return GameResult(GameResultCode.SUCCESS, message)
    }

    private fun shardTaken() {
        baseDescription = defaultRoomDescription
    }

    override fun saveRoomDataToDB(gameItems: ItemMap) {
        super.saveRoomDataToDB(gameItems)
        gameItems.addItem(key)
        gameItems.addItem(brokenMirror)
        gameItems.addItem(shard)
    }

    override fun getBaseData(): RoomData {
        val itemsData = ArrayList<SerializableItemData>()
        for (item in items.values) {
            itemsData.add(item.getData())
        }
        return FirstRoomData(roomId, itemsData, wasExaminedBefore, baseDescription)
    }

    override fun loadFromDB(roomData: SerializableRoomData, gameItems: ItemMap) {
        val data = roomData as FirstRoomData
        wasExaminedBefore = data.wasExaminedBefore
        baseDescription = data.baseDescription
        super.loadFromDB(roomData, gameItems)
    }

    override fun peek(player: Player, direction: Direction): GameResult {
        if (direction == Direction.WEST && !door.isOpen) {
            return GameResult(GameResultCode.FAIL, "Yeah, no, you can't peek into the next room while the [${door.name}] is closed. Nice try, though. :)")
        }

        return super.peek(player, direction)
    }
}