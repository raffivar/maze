fun main() {
    val game = Game()
    while (!game.isOver) {
        println("Please enter command:")
        val command = readLine()
        println(game.executeCommand(command))
    }
}