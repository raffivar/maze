package map

import data.ItemData
import data.SerializableRoomData
import game.Constraint
import game.GameResult
import game.GameResultCode
import items.Tiger.TigerStatus
import items.*

class RoomWithTiger(roomId: String, private val tiger: Tiger, private val bowl: Bowl) : Room(roomId), SavableRoom {
    init {
        addItem(tiger)
        addItem(bowl)
        addConstraint(
            Direction.NORTH,
            Constraint(tiger::isAlive, "You don't really want to do that while the [${tiger.name}] is still alive.")
        )
        addConstraint(
            Direction.EAST,
            Constraint(tiger::isAlive, "You don't really want to do that while the [${tiger.name}] is still alive.")
        )
        addEventUponMovement(Direction.SOUTH, this::playerWentSouthEvent)
    }

    override fun triggerEntranceEvent(): GameResult {
        if (tiger.isAlive() && tiger.facingSouth) {
            return GameResult(
                GameResultCode.GAME_OVER,
                "Eaten by a tiger, b!tch! Try peeking into a room before entering it next time."
            )
        }
        return super.triggerEntranceEvent()
    }

    override fun peekResult(): GameResult {
        if (!items.containsKey(tiger.name)) {
            return super.peekResult()
        }
        tiger.timesPeekedAt++
        when (tiger.status) {
            TigerStatus.STANDARD -> {
                return when (tiger.timesPeekedAt) {
                    1 -> GameResult(GameResultCode.SUCCESS, "WOAH! there's an incredibly large [${tiger.name}] in that room.")
                    2 -> {
                        tiger.facingSouth = false
                        GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is now facing the other way!")
                    }
                    else -> GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is still facing the other way...")
                }
            }
            TigerStatus.SMELLS_POISON -> {
                return when (tiger.timesPeekedAt) {
                    1 -> GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is turning around, slowly.")
                    2 -> GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is getting closer to the bowl..!!")
                    else -> {
                        tiger.setEatsPoison()
                        GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is now eating from the bowl!")
                    }
                }
            }
            TigerStatus.EATS_POISON -> {
                return when (tiger.timesPeekedAt) {
                    1 -> GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is not looking so good...!!")
                    else -> {
                        bowl.setFoodEaten()
                        tiger.kill(this)
                        when (items.containsKey(tiger.name)) {
                            true -> GameResult(GameResultCode.SUCCESS, "The [${tiger.name}]... might be dead?")
                            false -> GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is no longer there..!! it might be dead?"
                            )
                        }
                    }
                }
            }
            TigerStatus.DEAD -> {
                return GameResult(GameResultCode.SUCCESS, "Looks like the [${tiger.name}] is not moving.")
            }
        }
    }

    private fun playerWentSouthEvent(): GameResult {
        return when (bowl.status) {
            Bowl.BowlStatus.PRE_EATEN -> GameResult(GameResultCode.SUCCESS, "You run south quickly, because you're scared. What a wimp.")
            Bowl.BowlStatus.POISONED -> {
                tiger.setSmellsPoison()
                tiger.facingSouth = true
                GameResult(GameResultCode.SUCCESS, "When moving south, you can hear some movement behind you. You escape quickly.")
            }
            Bowl.BowlStatus.POST_EATEN -> GameResult(GameResultCode.SUCCESS, "")
        }
    }

    override fun saveRoomDataToDB(gameItems: ItemMap) {
        for (item in items) {
            gameItems.addItem(item.value)
        }
    }

    override fun loadFromDB(roomData: SerializableRoomData, gameItems: ItemMap) {
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
}