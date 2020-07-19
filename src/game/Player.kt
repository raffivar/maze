package game

import items.Item
import items.Openable
import map.Direction
import map.Room
import java.util.*

class Player(var currentRoom: Room) {
    val inventory = TreeMap<String, Item>(String.CASE_INSENSITIVE_ORDER)
    //val inventory = HashMap<String, Item>()

    fun go(direction: Direction): GameResult {
        return currentRoom.move(this, direction)
    }

    fun take(item: Item) {
        inventory[item.name] = item
        currentRoom.removeItem(item)
    }

    fun use(itemToUse: Item, itemToUseOn: Item): GameResult {
        return itemToUse.useOn(this, itemToUseOn)
    }

    fun open(itemToOpen: Item): GameResult {
        if (itemToOpen !is Openable) {
            return GameResult(GameResultCode.FAIL, "Item [${itemToOpen.name}] is not something you can open!")
        }

        if (itemToOpen.isClosed) {
            GameResult(GameResultCode.FAIL, "[${itemToOpen.name}] is already open")
        }

        return itemToOpen.open(this)
    }
}