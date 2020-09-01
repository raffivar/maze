package game

interface IOHandler {
    fun readCommand(): String?
    fun printMessage(message: String)
}