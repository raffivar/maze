package items

import game.GameResult
import game.GameResultCode
import game.Player

class Pole(name: String, val rope: Rope) : RopeDependedItem(name), SavableItem {
    init {
        itemsToFunctions[rope] = this::tieRope
    }

    private fun tieRope(player: Player): GameResult {
        if (!hasNothingAttached) {
            return GameResult(GameResultCode.FAIL, "[${this.name}] already has rope attached.")
        }
        hasNothingAttached = false
        return GameResult(GameResultCode.SUCCESS, "You attached the [${rope.name}] to the [${this.name}].")
    }
}