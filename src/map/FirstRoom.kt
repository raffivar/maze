package map

import data.*
import game.Constraint
import game.GameResult
import game.GameResultCode

import items.*
import java.util.ArrayList

class FirstRoom : Room("firstRoom"), SavableRoom {
    private var bed: Bed
    private var key: Key
    private var lock: Lock
    private var door: Door
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
        door = Door()
        addItem(door)
        mirror = Mirror(this::mirrorBroken)
        addItem(mirror)
        brokenMirror = BrokenMirror()
        shard = Shard(this::shardTaken)
        addConstraint(Direction.WEST, Constraint(door::isClosed, "The [${door.name}] is closed."))
        door.addConstraintToOpen(Constraint(lock::isLocked, "Looks like you have to to do something about the [${lock.name}] first."))
    }

    override fun examine(): GameResult {
        return when (wasExaminedBefore) {
            false -> {
                wasExaminedBefore = !wasExaminedBefore
                baseDescription = defaultRoomDescription
                super.examine(firstTimeDescription)
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
        return GameResult(GameResultCode.SUCCESS, "${bed.description}\nYou discover a small [${key.name}] under it.")
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
        val message = "You broke the [${mirror.name}]. You monster. Look what you did to the floor."
        return GameResult(GameResultCode.SUCCESS, message)
    }

    private fun shardTaken() {
        baseDescription = defaultRoomDescription
    }


    override fun save(gameItems: ItemMap) {
        gameItems.add(bed)
        gameItems.add(key)
        gameItems.add(lock)
        gameItems.add(door)
        gameItems.add(mirror)
        gameItems.add(brokenMirror)
    }

    override fun getData(): RoomData {
        val itemsData = ArrayList<SerializableItemData>()
        for (item in items.values) {
            itemsData.add(item.getData())
        }
        return FirstRoomData(roomId, itemsData, wasExaminedBefore, baseDescription)
    }

    override fun loadRoom(roomData: SerializableRoomData, gameItems: ItemMap) {
        val data = roomData as FirstRoomData
        wasExaminedBefore = data.wasExaminedBefore
        baseDescription = data.baseDescription

        items.clear()
        for (itemData in roomData.itemsData) {
            val item = gameItems[itemData.name]
            item?.let {
                items.add(item)
            }
            if (item is SavableItem) {
                item.loadItem(itemData as ItemData)
            }
        }
    }
}