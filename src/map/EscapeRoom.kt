package map

import game.Constraint
import game.GameResult
import game.GameResultCode
import items.*

class EscapeRoom(rope: Rope, private val tiger: Tiger): Room("escapeRoom", "There's only a window and a pole in this room. You might want to peek out the window.") {
    private val pole = Pole("Pole", rope)
    private val window = Window("Window", pole, rope)

    init {
        addItem(pole)
        addItem(window)
        addConstraint(Direction.DOWN, Constraint(window::isClosed, "You try to climb down a closed [${window.name}]. You fail."))
        addConstraint(Direction.DOWN, Constraint(pole::hasNothingAttached, "You can't really jump from there, it's too high."))
        addConstraint(Direction.DOWN, Constraint(window::hasNothingAttached, "Man, you already tied the [${rope.name}] to the [${pole.name}]. Think a bit more."))
        addEventUponMovement(Direction.DOWN, this::playerFailedToEscapeEvent)
    }

    private fun playerFailedToEscapeEvent(direction: Direction): GameResult {
        if (tiger.isInForbiddenRoom()) {
            return GameResult(
                GameResultCode.GAME_OVER,
                "You were so close to escape...!! alas, you got caught by the guards last minute.\n" +
                        "This is what happens when you leave a corpse in an exposed area.")
        }
        return GameResult(GameResultCode.SUCCESS, "")
    }
}