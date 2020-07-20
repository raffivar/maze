package game

import actions.*
import map.MapBuilder
import java.util.*
import kotlin.system.exitProcess

class Game {
    private val actions = arrayListOf(Go(), Examine(), Take(), Use(), Open(), Inventory())
    private val commandsToAction = TreeMap<String, Action>(String.CASE_INSENSITIVE_ORDER)
    private var player: Player
    //private val commands = HashMap<String, (args: List<String>) -> String>()

    init {
        val helpAction = Help(actions)
        actions.add(helpAction)
        actions.add(Exit())
        populateCommandsAsActions()

        val map = MapBuilder().build()
        player = Player(map)
    }

    private fun populateCommandsAsActions() {
        for (action in actions) {
            commandsToAction[action.name] = action
        }
    }

    fun run() {
        println(getIntro())
        do {
            print("Please enter command: ")
            val command = readLine()
            val commandResult = executeCommand(command)
            var messageToPrint = commandResult.message
            if (commandResult.gameResultCode == GameResultCode.GAME_OVER) {
                messageToPrint += " [Game Over]"
            }
            println(messageToPrint)
        } while (commandResult.gameResultCode != GameResultCode.GAME_OVER)
        exitProcess(-1)
    }

    private fun getIntro(): String {
        var intro = "Welcome to the maze!\n"
        intro += "You've been thrown into the first room!\n"
        intro += "=============================================\n"
        return intro + executeCommand("help").message
    }

    private fun executeCommand(command: String?): GameResult {
        if (command.isNullOrBlank()) {
            return GameResult(GameResultCode.ERROR, "Command empty, please try again.")
        }
        val parsedCommand = command.split(" ")
        val commandToExecute = parsedCommand[0]
        val args = parsedCommand.subList(1, parsedCommand.size)
        val action = commandsToAction[commandToExecute] ?: return GameResult(
            GameResultCode.ERROR,
            "Command [$command] not found"
        )
        return action.execute(player, args)
    }
}