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
        var item = inventory[itemName]
        if (item != null) {
            return item.examine()
        }
        item = currentRoom.items[itemName]
        if (item != null) {
            return item.examine()
        }
        return GameResult(
            GameResultCode.FAIL, "Item [$itemName] does not exist in your inventory or in the current room"
        )
    }

    fun go(direction: Direction): GameResult {
        return currentRoom.move(this, direction)
    }

    fun take(itemName: String): GameResult {
        val item = currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL,
            "Item [$itemName] does not exist in the current room"
        )
        if (item !is Takable) {
            return GameResult(GameResultCode.FAIL, "Item [${item.name}] is not something you can take!")
        }
        inventory[item.name] = item
        currentRoom.removeItem(item)

        return GameResult(GameResultCode.SUCCESS, "Obtained [${item.name}]")
    }

    fun use(item1Name: String, item2Name: String): GameResult {
        val item1 = inventory[item1Name] ?: return GameResult(GameResultCode.FAIL, "Mo [$item1Name] in inventory")
        var item2 = inventory[item2Name]
        if (item2 != null) {
            return item1.useOn(this, item2)
        }
        item2 = currentRoom.items[item2Name]
        if (item2 != null) {
            return item1.useOn(this, item2)
        }
        return GameResult(
            GameResultCode.ERROR,
            "Item [$item2Name] does not exist in your inventory or in the current room"
        )
    }

    fun open(itemName: String): GameResult {
        val itemToOpen = currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL,
            "Item [$itemName] does not exist in the current room"
        )
        if (itemToOpen !is Openable) {
            return GameResult(GameResultCode.FAIL, "Item [${itemToOpen.name}] is not something you can open.")
        }
        if (!itemToOpen.isClosed) {
            return GameResult(GameResultCode.FAIL, "[${itemToOpen.name}] is already open")
        }
        return itemToOpen.open(this)
    }

    fun getInventory(): GameResult {
        return if (inventory.isEmpty()) {
            GameResult(GameResultCode.SUCCESS, "[Inventory is currently empty]")
        } else {
            GameResult(GameResultCode.SUCCESS, "Inventory: ${inventory.keys}")
        }
    }
}