package map

import data.ItemData
import data.SerializableRoomData
import game.Constraint
import game.GameResult
import game.GameResultCode
import items.*

class RoomWithTiger(roomId: String, private val tiger: Tiger, private val bowl: Bowl) : Room(roomId), SavableRoom {
    var timesExaminedPrePoison = 0
    var timesExaminedPostPoison = 0

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
        if (tiger.isAlive && tiger.facingSouth) {
            return GameResult(GameResultCode.GAME_OVER, "The tiger ate you, b!tch.")
        }
        return super.triggerEntranceEvent()
    }

    override fun peekResult(): GameResult {
        if (tiger.isAlive)
            if (!tiger.isPoisoned) {
                timesExaminedPrePoison++
                return when (timesExaminedPrePoison) {
                    1 -> {
                        GameResult(GameResultCode.SUCCESS, "WOAH! there's an incredibly large [${tiger.name}] in that room.")
                    }
                    2 -> {
                        tiger.facingSouth = false
                        GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is now facing the other way!")
                    }
                    else -> {
                        GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is still facing the other way...")
                    }
                }
            }
        timesExaminedPostPoison++
        return when (timesExaminedPostPoison) {
            1 -> {
                GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is eating from the bowl!")
            }
            2 -> {
                GameResult(GameResultCode.SUCCESS, "The [${tiger.name}] is not looking so good.")
            }
            else -> {
                if (tiger.isAlive) {
                    tiger.kill(this)
                }
                GameResult(GameResultCode.SUCCESS, "The [${tiger.name}]... might be dead?")
            }
        }
    }

    private fun playerWentSouthEvent(): GameResult {
        if (bowl.hasPoison) {
            tiger.poison()
            tiger.facingSouth = true
            return GameResult(GameResultCode.SUCCESS, "player went south after putting poison in the bowl.")
        }
        return GameResult(GameResultCode.SUCCESS, "player went south without putting poison in the bowl.")
    }

    override fun saveRoom(gameItems: ItemMap) {
        for (item in items) {
            gameItems.addItem(item.value)
        }
    }

    override fun loadRoom(roomData: SerializableRoomData, gameItems: ItemMap) {
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