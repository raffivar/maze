package game

class CmdIOHandler : IOHandler {
    override fun read(): String? {
        return readLine()
    }

    override fun print(message: String) {
        kotlin.io.print(message)
    }
}