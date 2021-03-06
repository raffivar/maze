package map.rooms

import actions.constraints.Constraint
import game.GameResult
import game.GameResultCode
import player.Player
import items.*
import map.directions.Direction
import map.rooms.interfaces.HasEnterEvent
import map.rooms.interfaces.HasExitEvent
import map.rooms.interfaces.HasPeekEvent

class RoomWithTiger(roomId: String, private val tiger: Tiger, private val bowl: Bowl) : Room(roomId, "This room's floor seems to be covered with some kind of furry material."), HasPeekEvent, HasEnterEvent, HasExitEvent {
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
    }

    private fun addTiger(tiger: Tiger){
        addItem(tiger)
        tiger.startRoomId = this.roomId
        tiger.currentRoomId = this.roomId
    }

    override fun onRoomPeeked(defaultResult: () -> GameResult, player: Player): GameResult {
        if (!items.containsKey(tiger.name)) {
            return defaultResult.invoke()
        }

        return tiger.peekedAt(defaultResult, player, this)
    }

    override fun onEntered(defaultResult: GameResult, player: Player): GameResult {
        return when (items.containsKey(tiger.name) && tiger.isAlive() && tiger.status != Tiger.TigerStatus.FACING_NORTH) {
            true -> GameResult(GameResultCode.GAME_OVER, "You just got mauled by a gigantic tiger! Try peeking into a room before entering it next time.")
            false -> defaultResult
        }
    }

    override fun onExited(direction: Direction, player: Player): GameResult? {
        return when (direction) {
            Direction.SOUTH -> {
                when (bowl.status) {
                    Bowl.BowlStatus.PRE_EATEN -> GameResult(GameResultCode.SUCCESS, "You run [${direction.name}] quickly, because you're scared. What a wimp.")
                    Bowl.BowlStatus.POISONED -> {
                        tiger.setSmellsPoison()
                        GameResult(GameResultCode.SUCCESS, "When moving [${direction.name}], you can hear some movement behind you. You escape quickly.")
                    }
                    Bowl.BowlStatus.POST_EATEN -> null
                }
            }
            else -> null
        }
    }
}