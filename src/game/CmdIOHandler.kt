package game

class CmdIOHandler : IOHandler {
    override fun readCommand(): String? {
        print("Please enter command: ")
        return readLine()
    }

    override fun printMessage(message: String) {
        println(message)
        println("==========================================================================================================================")
    }
}