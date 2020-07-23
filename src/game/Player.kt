package game

import items.Item
import items.Openable
import items.Takable
import map.Direction
import map.Room
import java.util.*

class Player(var currentRoom: Room) {
    val inventory = TreeMap<String, Item>(String.CASE_INSENSITIVE_ORDER)
    //val inventory = HashMap<String, Item>()

    fun examine(objectToExamine: String): GameResult {
        return if (objectToExamine == "room") {
            examineCurrentRoom()
        } else {
            examineItem(objectToExamine)
        }
    }

    private fun examineCurrentRoom(): GameResult {
        return currentRoom.getDescription()
    }

    private fun examineItem(itemName: String): GameResult {
        val item = inventory[itemName] ?: currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL,
            "No [$itemName] in inventory or current room"
        )
        return item.examine()
    }

    fun go(direction: Direction): GameResult {
        return currentRoom.move(this, direction)
    }

    fun take(itemName: String): GameResult {
        val item = currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL, "No [$itemName] in current room"
        )
        if (item !is Takable) {
            return GameResult(GameResultCode.FAIL, "[${item.name}] is not something you can take")
        }
        inventory[item.name] = item
        currentRoom.removeItem(item)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${item.name}]")
    }

    fun open(itemName: String): GameResult {
        val itemToOpen =
            currentRoom.items[itemName] ?: return GameResult(GameResultCode.FAIL, "No [$itemName] in current room")
        if (itemToOpen !is Openable) {
            return GameResult(GameResultCode.FAIL, "Item [${itemToOpen.name}] is not something you can open")
        }
        if (!itemToOpen.isClosed) {
            return GameResult(GameResultCode.FAIL, "[${itemToOpen.name}] is already open")
        }
        return itemToOpen.open(this)
    }

    fun use(item1Name: String, item2Name: String): GameResult {
        val item1 = inventory[item1Name] ?: return GameResult(GameResultCode.FAIL, "No [$item1Name] in inventory")
        val item2 = inventory[item2Name] ?: currentRoom.items[item2Name] ?: return GameResult(
            GameResultCode.FAIL,
            "No [$item2Name] in inventory or current room"
        )
        return item1.useOn(this, item2)
    }

    fun getInventory(): GameResult {
        return if (inventory.isEmpty()) {
            GameResult(GameResultCode.SUCCESS, "[Inventory is currently empty]")
        } else {
            GameResult(GameResultCode.SUCCESS, "Inventory: ${inventory.keys}")
        }
    }
}