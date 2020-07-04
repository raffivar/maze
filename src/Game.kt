import items.Takable
import map.Direction
import map.Exit
import map.MapBuilder
import java.util.*

class Game {
    var isOver = false
        private set
    private val mapBuilder = MapBuilder()
    private val initialRoom = mapBuilder.build()
    private val player = Player()
    private val commandsToFuncs = TreeMap<String, (args: List<String>?) -> String>(String.CASE_INSENSITIVE_ORDER)
    //private val commands = HashMap<String, (args: List<String>) -> String>()

    init {
        player.currRoom = initialRoom
        initAvailableActions()
    }

    private fun initAvailableActions() {
        commandsToFuncs["go"] = ::go
        commandsToFuncs["examine"] = ::examine
        commandsToFuncs["take"] = ::take
        commandsToFuncs["inventory"] = ::inventory
        commandsToFuncs["help"] = ::getHelpMenu
    }

    fun getIntro(): String {
        var intro = "Welcome to the maze!\n"
        intro += "You've been thrown into the first room!\n"
        intro += "=============================================\n"
        intro += getHelpMenu(null)
        return intro
    }

    fun executeCommand(command: String?): String {
        if (command.isNullOrBlank()) {
            return "Command empty, please try again."
        }
        val parsedCommand = command.split(" ")
        val commandToExecute = parsedCommand[0]
        if (commandToExecute.equals("help", true)) {
            return getHelpMenu(null)
        }
        val commandArguments = parsedCommand.subList(1, parsedCommand.size)
        val funToExecute = commandsToFuncs[commandToExecute]
        return funToExecute?.invoke(commandArguments) ?: "Command [$commandToExecute] not found"
    }

    private fun go(args: List<String>?): String {
        if (args.isNullOrEmpty()) {
            return "Please choose a direction"
        }
        val directionAsText = args[0]
        val direction = Direction.values().find { it.name.equals(directionAsText, true) } ?: return "Invalid direction"
        val nextRoom = player.currRoom.rooms[direction] ?: return "This room does not lead to that direction"
        player.currRoom = nextRoom
        return if (player.currRoom is Exit) {
            isOver = true
            "Congrats, game over!"
        } else {
            "You moved ${direction.name}\n${player.currRoom.getDescription()}"
        }
    }

    private fun examine(args: List<String>?): String {
        if (args.isNullOrEmpty()) {
            return "Please choose either the room or an item to examine"
        }
        val itemName = args[0]
        if (itemName == "room") {
            return player.currRoom.getDescription()
        }
        var item = player.inventory[itemName]
        if (item != null) {
            return item.desc
        }
        item = player.currRoom.items[itemName]
        if (item != null) {
            return item.desc
        }
        return "Item [$itemName] does not exist in your inventory or in the current room"
    }

    private fun take(args: List<String>?): String {
        if (args.isNullOrEmpty()) {
            return "Please choose an item in the room to take"
        }
        val itemName = args[0]
        val item = player.currRoom.items[itemName]
            ?: return "Item [$itemName] does not exist in the current room"
        return if (item is Takable) {
            player.take(item)
            "Obtained [${item.name}]"
        } else {
            "Item [${item.name}] is not something you can take!"
        }
    }

    private fun inventory(args: List<String>?): String {
        return if (player.inventory.isEmpty()) {
            "[Inventory is currently empty]"
        } else {
            "Inventory: ${player.inventory.keys}"
        }
    }

    private fun getHelpMenu(args: List<String>?): String {
        var helpMenu = "The operations available to you are:\n"
        helpMenu += "GO [DIRECTION]\n"
        helpMenu += "EXAMINE [room/item in room]\n"
        helpMenu += "TAKE [item in room]\n"
        helpMenu += "HELP - prints this menu\n"
        return helpMenu
    }
}