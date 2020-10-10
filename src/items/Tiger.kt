package items

import data.ItemData
import data.TigerData
import game.*
import map.Room
import java.util.*
import kotlin.collections.HashMap

class Tiger : Item("Tiger", null), SavableItem {
    enum class TigerStatus { STANDARD, SMELLS_POISON, EATS_POISON, DEAD }
    var timesPeekedAt = 0
    var status = TigerStatus.STANDARD
    var facingSouth = true
    private val aliveDescription = "This is a really big, fat, lazy [$name]."
    private val deadDescription = "This gigantic [$name] seems to be dead."
    lateinit var currentRoomId: String
    lateinit var possibleDeathRooms: HashMap<String, Room>
    lateinit var forbiddenToLeaveRooms: HashMap<String, Room>

    fun isAlive(): Boolean {
        return status != TigerStatus.DEAD
    }

    override fun examine(): GameResult {
        return when (status) {
            TigerStatus.DEAD -> examine(deadDescription)
            else -> examine(aliveDescription)
        }
    }

    override fun take(player: Player): GameResult {
        return when (status) {
            TigerStatus.DEAD -> {
                player.inventory.addItem(this)
                player.currentRoom.removeItem(this)
                GameResult(GameResultCode.SUCCESS, "Obtained an extremely heavy [${this.name}]. Good luck with that.")
            }
            else -> {
                GameResult(GameResultCode.FAIL, "Seriously?? When the [${this.name}] is still alive??")
            }
        }

    }

    override fun drop(player: Player): GameResult {
        this.currentRoomId = player.currentRoom.roomId
        return super.drop(player)
    }

    private fun setTigerStatus(newStatus: TigerStatus) {
        status = newStatus
        timesPeekedAt = 0
    }

    fun setSmellsPoison() {
        setTigerStatus(TigerStatus.SMELLS_POISON)
    }

    fun setEatsPoison() {
        setTigerStatus(TigerStatus.EATS_POISON)
    }

    fun kill(startRoom: Room) {
        setTigerStatus(TigerStatus.DEAD)
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
        return TigerData(name, status, timesPeekedAt, facingSouth, currentRoomId)
    }

    override fun loadItem(itemData: ItemData) {
        val data = itemData as TigerData
        status = data.status
        timesPeekedAt = data.timesPeeked
        facingSouth = data.facingSouth
        currentRoomId = data.currentRoomId
    }
}