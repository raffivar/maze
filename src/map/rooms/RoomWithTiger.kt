package map.rooms

import actions.constraints.Constraint
import game.GameResult
import game.GameResultCode
import player.Player
import items.*
import map.directions.Direction
import map.rooms.interfaces.EnterEventRoom
import map.rooms.interfaces.PeekEventRoom

class RoomWithTiger(roomId: String, private val tiger: Tiger, private val bowl: Bowl) : Room(roomId, "This room reeks of fur. The floor is covered with it.", "This room's floor seems to be covered with some kind of furry material."), PeekEventRoom, EnterEventRoom {
    init {
        addTiger(tiger)
        addItem(bowl)
        addConstraint(
            Direction.NORTH,
            Constraint(
                tiger::isAlive,
                "You don't really want to do that while the [${tiger.name}] is still alive."
            )
        )
        addConstraint(
            Direction.EAST,
            Constraint(
                tiger::isAlive,
                "You don't really want to do that while the [${tiger.name}] is still alive."
            )
        )
        addEventUponMovement(Direction.SOUTH, this::playerWentSouthEvent)
    }

    private fun addTiger(tiger: Tiger){
        addItem(tiger)
        tiger.startRoomId = this.roomId
        tiger.currentRoomId = this.roomId
    }

    override fun onRoomEntered(defaultResult: () -> GameResult, player: Player): GameResult {
        return when (tiger.isAlive()) {
            true -> {
                when (tiger.facingSouth) {
                    true -> GameResult(GameResultCode.GAME_OVER, "You just got mauled by a gigantic tiger! Try peeking into a room before entering it next time.")
                    false -> defaultResult.invoke()
                }
            }
            false ->  {
                if (tiger.timesPeekedAt == 0) {
                    tiger.timesPeekedAt++
                }
                defaultResult.invoke()
            }
        }
    }

    override fun onRoomPeeked(defaultResult: () -> GameResult, player: Player): GameResult {
        if (!items.containsKey(tiger.name)) {
            return defaultResult.invoke()
        }

        return tiger.peekedAt(defaultResult, player, this)
    }

    private fun playerWentSouthEvent(direction: Direction): GameResult {
        return when (bowl.status) {
            Bowl.BowlStatus.PRE_EATEN -> GameResult(GameResultCode.SUCCESS, "You run [${direction.name}] quickly, because you're scared. What a wimp.")
            Bowl.BowlStatus.POISONED -> {
                tiger.setSmellsPoison()
                tiger.facingSouth = true
                GameResult(GameResultCode.SUCCESS, "When moving [${direction.name}], you can hear some movement behind you. You escape quickly.")
            }
            Bowl.BowlStatus.POST_EATEN -> GameResult(GameResultCode.SUCCESS, "")
        }
    }
}