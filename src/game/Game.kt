package game

import actions.*
import map.MapBuilder
import java.util.*
import kotlin.collections.ArrayList

class Game {
    private val mapBuilder = MapBuilder()
    private val initialRoom = mapBuilder.build()
    private val player = Player(initialRoom)
    private val actions = listOf(Go(), Examine(), Take(), Inventory(), Help())
    private val commandsToAction = TreeMap<String, Action>(String.CASE_INSENSITIVE_ORDER)
    //private val commands = HashMap<String, (args: List<String>) -> String>()

    init {
        populateActions()
    }

    private fun populateActions() {
        for (action in actions) {
            commandsToAction[action.name] = action
        }
    }

    fun run() {
        println(getIntro())
        do {
            print("Please enter command: ")
            val command = readLine()
            val result = executeCommand(command)
            println(result.message)
        } while (result.gameResultCode != GameResultCode.GAME_OVER)

    }

    private fun getIntro(): String {
        var intro = "Welcome to the maze!\n"
        intro += "You've been thrown into the first room!\n"
        intro += "=============================================\n"
        return intro + executeCommand("help").message
    }

    private fun executeCommand(command: String?): GameResult {
        if (command.isNullOrBlank()) {
            return GameResult(
                GameResultCode.ERROR,
                "Command empty, please try again."
            )
        }
        val parsedCommand = command.split(" ")
        val commandToExecute = parsedCommand[0]
        val args = parsedCommand.subList(1, parsedCommand.size)
        val action = commandsToAction[commandToExecute] ?: return GameResult(
            GameResultCode.ERROR,
            "Command not found"
        )
        return action.execute(player, args)
    }
}