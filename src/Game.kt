import java.util.*

class Game {
    var isOver = false
        private set
    private val mapBuilder = MapBuilder()
    private val initialRoom = mapBuilder.build()
    private val player = Player()
    //private val commands = HashMap<String, (args: List<String>) -> String>()
    private val commands =  TreeMap<String, (args: List<String>) -> String>(String.CASE_INSENSITIVE_ORDER)

    init {
        player.currRoom = initialRoom
        initAvailableActions()
    }

    private fun initAvailableActions() {
        commands["go"] = ::go
        commands["examine"] = ::examine
        commands["end"] = ::endGame    }

    private fun go(args: List<String>): String {
        if (args.isNullOrEmpty()) {
            return "Please choose a direction"
        }
        val directionAsText = args[0]
        val direction = Direction.values().find { it.name.equals(directionAsText, true) } ?: return "Invalid direction"
        val nextRoom = player.currRoom.rooms[direction] ?: return "This room does not lead to that direction"
        player.currRoom = nextRoom
        return "You moved ${direction.name}"
    }

    private fun examine(args: List<String>): String {
        if (args.isNullOrEmpty()) {
            return "Please choose either the room or an item to examine"
        }
        return when (args[0]) {
            "room" -> player.currRoom.desc
            else -> "Item ${args[0]} does not exist in your inventory or in the current room"
        }
    }

    private fun endGame(args: List<String>): String {
        isOver = true
        return "Game Over"
    }

    fun executeCommand(command: String?): String {
        if (command.isNullOrBlank()) {
            return "Command empty, please try again."
        }
        val parsedCommand = command.split(" ")
        val commandToExecute = parsedCommand[0]
        val commandArguments = parsedCommand.subList(1, parsedCommand.size)
        val funToExecute = commands[commandToExecute]
        return funToExecute?.invoke(commandArguments) ?: "Command [$commandToExecute] not found"
    }
}