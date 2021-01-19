package map.rooms

import actions.constraints.Constraint
import game.GameResult
import game.GameResultCode
import items.*
import map.directions.Direction
import map.rooms.interfaces.ExitEventRoom
import player.Player

class LastRoom(rope: Rope, private val tiger: Tiger): Room("lastRoom", "There's only a window and a pole in this room. You might want to peek out the window."), ExitEventRoom {
    private val pole = Pole("Pole", rope)
    private val window = Window("Window", pole, rope)

    init {
        addItem(pole)
        addItem(window)
        addConstraint(
            Direction.DOWN,
            Constraint(
                { !window.isOpen },
                "The [${window.name}] is closed."
            )
        )
        addConstraint(
            Direction.DOWN,
            Constraint(
                pole::hasNothingAttached,
                "It's too high."
            )
        )
        addConstraint(
            Direction.DOWN,
            Constraint(
                window::hasNothingAttached,
                "Man, you already tied the [${rope.name}] to the [${pole.name}]. Think a bit more."
            )
        )
    }

    override fun onRoomExited(direction: Direction, player: Player): GameResult? {
        when (direction) {
            Direction.DOWN -> {
                if (tiger.isInForbiddenRoom()) {
                    return GameResult(GameResultCode.GAME_OVER,
                        "You were so close to escape...!! alas, you got caught by the guards last minute.\n" +
                                "This is what happens when you leave a corpse in an exposed area.\n" +
                                "You might want to pay close attention to the [${tiger.name}] and where you leave it...")
                }
                return GameResult(GameResultCode.SUCCESS, "")
            }
            else -> return null
        }
    }
}