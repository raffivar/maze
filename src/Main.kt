import game.CmdIOHandler
import game.Game

fun main() {
    val game = Game(CmdIOHandler())
    game.run()
}