import game.CmdIOHandler
import game.Game

fun main(args: Array<String>) {
    val game = Game(CmdIOHandler())
    game.run()
}