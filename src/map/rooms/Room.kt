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

open class Room(val roomId: String = "", var baseDescription: String = "Just a regular room.", itemsToAdd: ArrayList<Item>? = null) {
    val items = ItemMap()
    private val rooms = HashMap<Direction, Room>()
    private val constraintsToMove = HashMap<Direction, ArrayList<Constraint>>()
    private val eventsUponMovement = HashMap<Direction, ArrayList<(Direction) -> GameResult>>()

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

    open fun peekResult(player: Player): GameResult {
        return examine()
    }

    open fun getFirstLook(): GameResult {
        return examine()
    }

    open fun examine(): GameResult {
        return examine(null)
    }

    open fun examineWithPrefix(prefix: String?): GameResult {
        return examine(prefix)
    }

    open fun examine(prefix: String?): GameResult {
        var description = baseDescription + "\n" + getExtraDetails()
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
            constraintsToMove[direction] ?: ArrayList<Constraint>().also { constraintsToMove[direction] = it }
        constraints.add(constraint)
    }

    fun addEventUponMovement(direction: Direction, event: (Direction) -> (GameResult)) {
        val events = eventsUponMovement[direction]
            ?: ArrayList<(Direction) -> GameResult>().also { eventsUponMovement[direction] = it }
        events.add(event)
    }

    fun move(player: Player, direction: Direction): GameResult {
        val nextRoom =
            rooms[direction] ?: return GameResult(GameResultCode.FAIL, "This room does not lead [$direction]")

        val constraints = constraintsToMove[direction]
        constraints?.let {
            for (constraint in it) {
                if (constraint.isConstraining.invoke()) {
                    return GameResult(GameResultCode.FAIL, "Cannot move [${direction.name}] - ${constraint.message}")
                }
            }
        }

        var eventsMessage = ""
        val events = eventsUponMovement[direction]
        events?.let {
            for (event in it) {
                val eventResult = event.invoke(direction)
                when (eventResult.gameResultCode) {
                    GameResultCode.GAME_OVER -> return eventResult
                    else -> {
                        if (eventResult.message.isNotBlank()) {
                            eventsMessage += eventResult.message
                        }
                    }
                }
            }
        }

        player.currentRoom = nextRoom

        val moveMessage = "Moved [${direction.name}]."
        return when (eventsMessage.isBlank()) {
            true -> GameResult(GameResultCode.SUCCESS, moveMessage)
            false -> GameResult(GameResultCode.SUCCESS, "$eventsMessage\n$moveMessage")
        }
    }

    open fun peek(player: Player, direction: Direction): GameResult {
        val roomToPeek =
            rooms[direction] ?: return GameResult(GameResultCode.FAIL, "This room does not lead [$direction]")
        return roomToPeek.peekResult(player)
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