package game

interface IOHandler {
    fun read(): String?
    fun print(message: String)
}