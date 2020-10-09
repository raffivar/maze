package map

import data.SerializableItemData
import data.RoomData
import game.*
import items.Item
import items.ItemMap
import items.Tiger
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class Room(val roomId: String = "", var baseDescription: String = "Just a regular room.") {
    val items = ItemMap()
    val rooms = HashMap<Direction, Room>()
    private val constraintsToMove = HashMap<Direction, ArrayList<Constraint>>()
    private val eventsUponMovement = HashMap<Direction, ArrayList<() -> GameResult>>()

    open fun triggerEntranceEvent(): GameResult  {
        return examine()
    }

    open fun peekResult(): GameResult {
        return examine()
    }

    open fun examine(): GameResult {
        return examine(null)
    }

    open fun examine(externalDescription: String?): GameResult {
        var description = (externalDescription ?: baseDescription) + "\n"
        description += if (items.isEmpty()) {
            "This room is empty.\n"
        } else {
            "Items in room: ${items.values}.\n"
        }
        description += if (rooms.isEmpty()) {
            "It seems this room doesn't lead anywhere else."
        } else {
            "This room leads: ${rooms.keys}."
        }
        return GameResult(GameResultCode.SUCCESS, description)
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
        val constraints = constraintsToMove[direction] ?: ArrayList<Constraint>().also { constraintsToMove[direction] = it }
        constraints.add(constraint)
    }

    fun addEventUponMovement(direction: Direction, event: () -> (GameResult)) {
        val events = eventsUponMovement[direction] ?: ArrayList<() -> GameResult>().also { eventsUponMovement[direction] = it }
        events.add(event)
    }

    fun move(player: Player, direction: Direction): GameResult {
        val nextRoom = rooms[direction] ?: return GameResult(GameResultCode.FAIL, "This room does not lead [$direction]")

        val constraints = constraintsToMove[direction]
        constraints?.let {
            for (constraint in it) {
                if (constraint.isConstraining.invoke()) {
                    return GameResult(GameResultCode.FAIL, "Cannot move [${direction.name}] - ${constraint.message}")
                }
            }
        }

        var message = ""
        val events = eventsUponMovement[direction]
        events?.let {
            for (event in it) {
                val eventResult = event.invoke()
                when (eventResult.gameResultCode) {
                    GameResultCode.GAME_OVER -> return eventResult
                    else -> {
                        if (!message.isBlank())  {
                            message += eventResult.message + "\n"
                        }
                    }
                }
            }
        }

        player.currentRoom = nextRoom
        message + "Moved ${direction.name}"
        return GameResult(GameResultCode.SUCCESS, message)
    }

    open fun getBaseData(): RoomData {
        val itemsData = ArrayList<SerializableItemData>()
        for (item in items.values) {
            itemsData.add(item.getData())
        }
        return RoomData(roomId, itemsData)
    }

    open fun peek(direction: Direction): GameResult {
        val roomToPeek = rooms[direction] ?: return GameResult(GameResultCode.FAIL, "This room does not lead [$direction]")
        return roomToPeek.peekResult()
    }
}