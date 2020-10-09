package items

import data.ItemData
import data.TigerData
import game.*
import map.Room
import java.util.*
import kotlin.collections.HashMap

class Tiger : Item("Tiger", null), SavableItem {
    var isAlive = true
    var isPoisoned = false
    var facingSouth = true
    private val aliveDescription = "This is a really big, fat, lazy [$name]."
    private val deadDescription = "This gigantic [$name] seems to be dead."
    lateinit var currentRoomId: String
    lateinit var possibleDeathRooms: HashMap<String, Room>
    lateinit var forbiddenToLeaveRooms: HashMap<String, Room>

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

    override fun drop(player: Player): GameResult {
        this.currentRoomId = player.currentRoom.roomId
        return super.drop(player)
    }

    fun poison() {
        isPoisoned = true
    }

    fun kill(startRoom: Room) {
        isAlive = false
        moveRoomUponDeath(startRoom)
    }

    private fun moveRoomUponDeath(startRoom: Room) {
        val random = Random()
        if (possibleDeathRooms.size == 0) {
            return
        }
        val randomIndex = random.nextInt(possibleDeathRooms.size)
        val deathRoom = possibleDeathRooms.entries.elementAt(randomIndex).value
        if (deathRoom.roomId != startRoom.roomId) {
            startRoom.removeItem(this)
            deathRoom.addItem(this)
            currentRoomId = deathRoom.roomId
        }
    }

    fun isInForbiddenRoom(): Boolean {
        return forbiddenToLeaveRooms.containsKey(currentRoomId)
    }

    override fun getData(): ItemData {
        return TigerData(name, isAlive, isPoisoned, facingSouth, currentRoomId)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as TigerData
        isAlive = data.isAlive
        isPoisoned = data.isPoisoned
        facingSouth = data.facingSouth
        currentRoomId = data.currentRoomId
    }
}