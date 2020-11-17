package game.interfaces

interface IOHandler {
    fun readCommand(): String?
    fun printMessage(message: String)
}