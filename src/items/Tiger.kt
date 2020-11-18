package items

import data.items.ItemData
import data.items.TigerData
import game.*
import items.interfaces.SavableItem
import map.rooms.Room
import player.Player
import java.util.*
import kotlin.collections.HashMap

class Tiger(private val bowl: Bowl) : Item("Tiger", null), SavableItem {
    enum class TigerStatus { STANDARD, SMELLS_POISON, EATS_POISON, DEAD }
    var timesPeekedAt = 0
    var status = TigerStatus.STANDARD
    var facingSouth = true
    private val aliveDescription = "This is a really big, fat, lazy [$name]."
    private val deadDescription = "This gigantic [$name] seems to be dead."
    lateinit var startRoomId: String
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
                GameResult(GameResultCode.FAIL, "Seriously?? When the [${this.name}] is still alive?? No way. You're stupid, but not *THAT* stupid.")
            }
        }
    }

    override fun breakItem(player: Player): GameResult {
        return GameResult(GameResultCode.FAIL, "Err... Yeah, you choose to NOT attempt to do that, especially when the [${this.name}] is still alive.")
    }

    override fun drop(player: Player): GameResult {
        this.currentRoomId = player.currentRoom.roomId
        return super.drop(player)
    }

    fun peekedAt(player: Player, room: Room): GameResult {
        timesPeekedAt++
        return when (status) {
            TigerStatus.STANDARD -> {
                when (timesPeekedAt) {
                    1 -> GameResult(GameResultCode.SUCCESS, "WOAH! there's an incredibly large [${this.name}] in that room.")
                    2 -> {
                        this.facingSouth = false
                        GameResult(GameResultCode.SUCCESS, "The [${this.name}] is now facing the other way!")
                    }
                    else -> GameResult(GameResultCode.SUCCESS, "The [${this.name}] is still facing the other way...")
                }
            }
            TigerStatus.SMELLS_POISON -> {
                when (timesPeekedAt) {
                    1 -> GameResult(GameResultCode.SUCCESS, "The [${this.name}] is turning around, slowly.")
                    2 -> GameResult(GameResultCode.SUCCESS, "The [${this.name}] is getting closer to the bowl..!!")
                    else -> {
                        this.setEatsPoison()
                        GameResult(GameResultCode.SUCCESS, "The [${this.name}] is now eating from the bowl!")
                    }
                }
            }
            TigerStatus.EATS_POISON -> {
                when (timesPeekedAt) {
                    1 -> GameResult(GameResultCode.SUCCESS, "The [${this.name}] has eaten the whole thing, and is not looking so good...!!")
                    else -> {
                        bowl.setFoodEaten()
                        this.kill(room)
                        when (currentRoomId == startRoomId) {
                            true -> GameResult(GameResultCode.SUCCESS, "The [${this.name}] is just laying there, on the floor...")
                            false -> {
                                when (currentRoomId == player.currentRoom.roomId) {
                                    true -> GameResult(GameResultCode.SUCCESS, "The [${this.name}] slowly waddles into the room you're in, falls heavily on the floor, and stops moving.")
                                    false -> GameResult(GameResultCode.SUCCESS, "The [${this.name}] is no longer there..! Good chance it just collapsed in another room.")
                                }
                            }
                        }
                    }
                }
            }
            TigerStatus.DEAD -> {
                when (timesPeekedAt) {
                    1 -> GameResult(GameResultCode.SUCCESS, "Looks like the [${this.name}] is stopped moving.")
                    else -> return room.examine()
                }
            }
        }
    }

    fun setSmellsPoison() {
        setTigerStatus(TigerStatus.SMELLS_POISON)
    }

    private fun setEatsPoison() {
        setTigerStatus(TigerStatus.EATS_POISON)
    }

    private fun kill(startRoom: Room) {
        setTigerStatus(TigerStatus.DEAD)
        moveRoomUponDeath(startRoom)
    }

    private fun setTigerStatus(newStatus: TigerStatus) {
        status = newStatus
        timesPeekedAt = 0
    }

    private fun moveRoomUponDeath(currentRoom: Room) {
        val random = Random()
        if (possibleDeathRooms.size == 0) {
            return
        }
        val randomIndex = random.nextInt(possibleDeathRooms.size)
        val deathRoom = possibleDeathRooms.entries.elementAt(randomIndex).value
        if (deathRoom.roomId != currentRoom.roomId) {
            currentRoom.removeItem(this)
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