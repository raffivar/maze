fun main() {
    val game = Game()
    println(game.getIntro())
    while (!game.isOver) {
        print("Please enter command: ")
        val command = readLine()
        println(game.executeCommand(command))
    }
}