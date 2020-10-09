package items

import game.GameResult
import game.GameResultCode
import game.Player

class Window(name: String, private val pole: Pole, private val rope: Rope = pole.rope) : RopeDependedItem(name) {
    init {
        itemsToFunctions[rope] = this::throwRope
    }

    private fun throwRope(player: Player): GameResult {
        if (!hasNothingAttached) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] already has rope thrown out of it.")
        }
        if (pole.hasNothingAttached) {
            return GameResult(GameResultCode.SUCCESS, "You might want to attach the [${rope.name}] to the [${pole.name}] first.")
        }
        hasNothingAttached = false
        return GameResult(GameResultCode.SUCCESS, "You threw the [${rope.name}] out the [${this.name}].")
    }
}