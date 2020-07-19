package map

import game.Constraint
import game.GameResult
import game.GameResultCode
import game.Player
import items.Item
import java.util.*
import kotlin.collections.HashMap

open class Room() {
    var desc: String = "Just a regular room"
    val items = TreeMap<String, Item>(String.CASE_INSENSITIVE_ORDER)
    //val items = HashMap<String, Item>()
    private val rooms = HashMap<Direction, Room>()
    private val constraintsToMove = HashMap<Direction, ArrayList<Constraint>>()

    fun getDescription(): String {
        var description = desc + "\n"
        description += if (items.isEmpty()) {
            "This room is empty\n"
        } else {
            "Items in room: ${items.keys}\n"
        }
        description += if (rooms.isEmpty()) {
            "It seems this room doesn't lead anywhere else\n"
        } else {
            "This room leads: ${rooms.keys}\n"
        }
        return description
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

    fun move(player: Player, direction: Direction): GameResult {
        val nextRoom = rooms[direction] ?: return GameResult(GameResultCode.FAIL, "This room does not lead to that direction")
        val constraints = constraintsToMove[direction]
        constraints?.let {
            for (constraint in it) {
                if (constraint.constrainingParty.invoke()) {
                    return GameResult(GameResultCode.FAIL, "You failed to move [${direction.name}] - ${constraint.message}")
                }
            }
        }
        player.currentRoom = nextRoom
        if (player.currentRoom is Exit) {
            return GameResult(GameResultCode.GAME_OVER, "You're out! Congrats!")
        }
        return GameResult(GameResultCode.SUCCESS, "You moved ${direction.name}\n${player.currentRoom.getDescription()}")
    }
}