package items

import data.ItemData
import data.TigerData
import game.*
import map.Room
import java.util.*

class Tiger : Item("Tiger", null), SavableItem {
    var isAlive = true
    var isPoisoned = false
    var facingSouth = true
    private val aliveDescription = "This is a really big, fat, lazy [$name]."
    private val deadDescription = "This gigantic [$name] seems to be dead."
    lateinit var endRoom: Room

    override fun examine(): GameResult {
        return when (isAlive) {
            true -> examine(aliveDescription)
            false -> examine(deadDescription)
        }
    }

    override fun take(player: Player): GameResult {
        if (this.isAlive) {
            return GameResult(GameResultCode.FAIL, "Seriously?? When the [${this.name}] is still alive??")
        }
        player.inventory.addItem(this)
        player.currentRoom.removeItem(this)
        return GameResult(GameResultCode.SUCCESS, "Obtained an extremely heavy [${this.name}]. Good luck with that.")
    }

    fun poison() {
        isPoisoned = true
    }

    fun kill(startRoom: Room) {
        isAlive = false
        moveRoom(startRoom)
    }

    private fun moveRoom(startRoom: Room) {
        val adjacentRooms = startRoom.rooms
        val random = Random()
        endRoom = adjacentRooms.entries.elementAt(random.nextInt(adjacentRooms.size)).value
        if (endRoom.roomId != startRoom.roomId) {
            startRoom.removeItem(this)
            endRoom.addItem(this)
        }
    }

    override fun getData() : ItemData {
        return TigerData(name, isAlive, isPoisoned, facingSouth)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as TigerData
        isAlive = data.isAlive
        isPoisoned = data.isPoisoned
        facingSouth = data.facingSouth
    }
}