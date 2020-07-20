package game

import actions.*
import map.MapBuilder
import java.util.*
import kotlin.system.exitProcess

class Game {
    private val actions = arrayListOf(Go(), Examine(), Take(), Use(), Open(), Inventory())
    private val actionsByName = TreeMap<String, Action>(String.CASE_INSENSITIVE_ORDER)
    //private val actionsByName = HashMap<String, (args: List<String>) -> String>()
    private var player: Player

    init {
        val helpAction = Help(actions)
        actions.add(helpAction)
        actions.add(Exit())
        populateActionsByNames()
        val map = MapBuilder().build()
        player = Player(map)
    }

    private fun populateActionsByNames() {
        for (action in actions) {
            actionsByName[action.name] = action
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
        val actionName = parsedCommand[0]
        val action = actionsByName[actionName] ?: return GameResult(GameResultCode.ERROR, "Action [$command] not found")
        val args = parsedCommand.subList(1, parsedCommand.size)
        return action.execute(player, args)
    }
}