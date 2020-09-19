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
    private var wasExaminedBefore = false
    private val firstTimeDescription = "You wake up in small room. There's only the bed you woke up on (which is horribly uncomfortable) and a door with a lock on it."
    private val defaultRoomDescription = "This room only contains a bed and a door with a lock on it."

    init {
        bed = Bed(this::bedExamined)
        addItem(bed)
        key = Key(this::keyTaken)
        lock = Lock(key)
        addItem(lock)
        door = Door()
        addItem(door)
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
        baseDescription = "This room has a closed door with a lock on it, but after examining the bed, you also notice a small key under it."
        return GameResult(GameResultCode.SUCCESS, "${bed.description}\nYou discover a small [${key.name}] under it.")
    }

    private fun keyTaken() {
        baseDescription = defaultRoomDescription
    }

    override fun save(gameItems: ItemMap) {
        gameItems.add(bed)
        gameItems[key.name] = key
        gameItems[lock.name] = lock
        gameItems[door.name] = door
    }

    override fun getData(): SavableRoomData {
        val itemsData = ArrayList<ItemData>()
        for (item in items.values) {
            itemsData.add(item.getData())
        }
        return FirstRoomData(roomId, itemsData, wasExaminedBefore, baseDescription)
    }

    override fun loadRoom(roomData: RoomData, gameItems: ItemMap) {
        val data = roomData as FirstRoomData
        wasExaminedBefore = data.wasExaminedBefore
        baseDescription = data.baseDescription

        items.clear()
        for (itemData in roomData.itemsData) {
            val item = gameItems[itemData.name]
            if (item is SavableItem) {
                item.loadItem(itemData as SavableItemData)
            }
            item?.let {
                items.add(item)
            }
        }
    }
}