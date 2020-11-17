package items

import game.GameResult
import game.GameResultCode
import player.Player

class BrokenMirror :
    Item("Broken_Mirror", "This mirror is broken. It is now only the shell of a mirror. Some people are just monsters.") {
    override fun take(player: Player): GameResult {
        return GameResult(GameResultCode.SUCCESS, "This is pinned to the wall.")
    }
}