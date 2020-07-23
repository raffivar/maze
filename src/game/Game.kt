package game

import actions.*
import map.MapBuilder
import java.lang.StringBuilder
import java.util.*
import kotlin.system.exitProcess

class Game {
    private val actions = arrayListOf(Go(), Examine(), Take(), Use(), Open(), Inventory(), Exit())
    private val helpAction = Help(actions)
    private val actionsByName = TreeMap<String, Action>(String.CASE_INSENSITIVE_ORDER)
    //private val actionsByName = HashMap<String, (args: List<String>) -> String>()
    private var player: Player

    init {
        actions.add(helpAction)
        populateActionsByNames()
        val firstRoom = MapBuilder().build()
        player = Player(firstRoom)
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
        val sb = StringBuilder()
        sb.append("Welcome to the maze!\n")
        sb.append("You've been thrown into the first room!\n")
        sb.append("=============================================\n")
        sb.append(helpAction.execute(player, listOf()).message)
        return sb.toString()
    }

    private fun executeCommand(command: String?): GameResult {
        if (command.isNullOrBlank()) {
            return GameResult(GameResultCode.ERROR, "Command empty, please try again.")
        }
        val parsedCommand = command.split(" ")
        val actionName = parsedCommand[0]
        val action = actionsByName[actionName] ?: return GameResult(GameResultCode.ERROR, "Action [$actionName] not found")
        val args = parsedCommand.subList(1, parsedCommand.size)
        return action.execute(player, args)
    }
}