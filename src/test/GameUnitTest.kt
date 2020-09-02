package test

import game.Game
import game.IOHandler

fun main() {
    val ioHandler = object : IOHandler {
        val pace = 1300L
        var i = 0
        val commands = arrayListOf(
            "examine room",
            "examine bed",
            "take key",
            "use key on lock",
            "open door",
            "go west",
            "go north",
            "go north",
            "go north",
            "go east",
            "examine bowl",
            "take bonzo",
            "go west",
            "use bonzo on dog",
            "go north"
        )

        override fun readCommand(): String? {
            val command = commands[i]
            Thread.sleep(pace)
            println("Command: $command")
            return command
        }

        override fun printMessage(message: String) {
            println(message)
            if (i == 13 && message == "No [dog] in inventory or current room") {
                println("[Waiting for dog to enter the room]")
                Thread.sleep(pace + 500)
            } else {
                i++
            }
            println("==================================================================================================================================")
        }

    }

    val game = Game(ioHandler)
    game.run()
}
