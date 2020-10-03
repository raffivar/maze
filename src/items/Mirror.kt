package items

import game.GameResult
import game.GameResultCode
import game.Player

class Mirror(private val modifyRoomWhenExamined: () -> GameResult) : Item("Mirror", "This mirror doesn't even have a frame! How untasteful!") {
    override fun breakItem(player: Player): GameResult {
        return modifyRoomWhenExamined()
    }

    override fun take(player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "This is pinned to the wall.")
    }
}