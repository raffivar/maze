package test

import game.Game
import game.IOHandler

fun main() {
    val ioHandler = object : IOHandler {
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
            println("Command: $command")
            return command
        }

        override fun printMessage(message: String) {
            println(message)
            if (i == 13 && message == "No [dog] in inventory or current room") {
                println("[Waiting for dog to enter the room]")
                Thread.sleep(500)
            } else {
                i++
            }
            println("==========================================================================================================================")
        }

    }

    val game = Game(ioHandler)
    game.run()
}
