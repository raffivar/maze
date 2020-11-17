package items

import game.GameResult
import game.GameResultCode
import player.Player

class BrokenMirror : Item("Broken_Mirror", "This mirror is broken. It is now only the shell of a mirror. Some people are just monsters.") {
    override fun take(player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "This... ex-mirror, is pinned to the wall.")
    }

    override fun breakItem(player: Player): GameResult {
        return GameResult(GameResultCode.FAIL, "You can't break something already so broken. Has this mirror not been through enough????")
    }
}