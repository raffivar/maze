package map.rooms

import actions.constraints.Constraint
import data.items.ItemData
import data.serializables.SerializableItemData
import data.rooms.RoomData
import data.serializables.SerializableRoomData
import game.*
import items.Item
import items.maps.ItemMap
import items.interfaces.SavableItem
import map.directions.Direction
import player.Player
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class Room(val roomId: String = "", var baseDescription: String = "Just a regular room.", private val peekDescription: String = baseDescription, itemsToAdd: ArrayList<Item>? = null) {
    val items = ItemMap()
    val rooms = HashMap<Direction, Room>()
    val constraintsToMoveOrPeek = HashMap<Direction, ArrayList<Constraint>>()
    val eventsUponMovement = HashMap<Direction, ArrayList<(Direction) -> GameResult>>()

    init {
        itemsToAdd?.let {
            for (item in itemsToAdd) {
                addItem(item)
            }
        }
    }

    open fun triggerEntranceEvent(moveResult: GameResult): GameResult {
        return examineWithPrefix(moveResult.message)
    }

    open fun getFirstLook(): GameResult {
        return examine()
    }

    open fun examine(): GameResult {
        return examine(null, null)
    }

    open fun peek(player: Player): GameResult {
        return examine(null, peekDescription)
    }

    open fun examineWithPrefix(prefix: String?): GameResult {
        return examine(prefix, null)
    }

    open fun examine(prefix: String?, baseDescription: String?): GameResult {
        var description = (baseDescription ?: this.baseDescription) + "\n"
        description += getExtraDetails()
        prefix?.let {
            if (it.isNotBlank()) {
                description = "$it\n$description"
            }
        }
        return GameResult(GameResultCode.SUCCESS, description)
    }

    private fun getExtraDetails(): String {
        var extraDetails = "This room contains: "
        extraDetails += if (items.isEmpty()) {
            "[Absolutely nothing].\n"
        } else {
            "${items.values}.\n"
        }

        extraDetails += "This room leads: "
        extraDetails += if (rooms.isEmpty()) {
            "[Absolutely nowhere]."
        } else {
            "${rooms.keys}."
        }

        return extraDetails
    }

    fun addItem(item: Item) {
        items[item.name] = item
    }

    fun removeItem(item: Item) {
        items.remove(item.name)
    }

    fun addRoom(direction: Direction, room: Room) {
        rooms[direction] = room
    }

    fun addConstraint(direction: Direction, constraint: Constraint) {
        val constraints =
            constraintsToMoveOrPeek[direction] ?: ArrayList<Constraint>().also { constraintsToMoveOrPeek[direction] = it }
        constraints.add(constraint)
    }

    fun addEventUponMovement(direction: Direction, event: (Direction) -> (GameResult)) {
        val events = eventsUponMovement[direction]
            ?: ArrayList<(Direction) -> GameResult>().also { eventsUponMovement[direction] = it }
        events.add(event)
    }

    open fun saveDataToDB(gameItems: ItemMap) {
        for (item in items) {
            gameItems.addItem(item.value)
        }
    }

    open fun loadDataFromDB(roomData: SerializableRoomData, gameItems: ItemMap) {
        items.clear()
        for (itemData in roomData.itemsData) {
            val item = gameItems[itemData.name]
            item?.let {
                items.addItem(item)
            }
            if (item is SavableItem) {
                item.loadItem(itemData as ItemData)
            }
        }
    }

    open fun getDataToSaveToFile(): RoomData {
        val itemsData = ArrayList<SerializableItemData>()
        for (item in items.values) {
            itemsData.add(item.getDataToSaveToFile())
        }
        return RoomData(roomId, itemsData)
    }
}