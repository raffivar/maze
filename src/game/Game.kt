package game

import actions.*
import map.MapBuilder
import java.lang.StringBuilder

class Game(private val ioHandler: IOHandler) {
    private val actions = arrayListOf(Go(), Examine(), Take(), Use(), Open(), Break(), Inventory(), Exit())
    private val helpAction = Help(actions)
    private val actionsByName = ActionMap()
    private val gameThreads = arrayListOf<Thread>()
    private val player: Player
    private val gameDataManager: GameDataManager

    init {
        val mapBuilder = MapBuilder(gameThreads)
        val firstRoom = mapBuilder.build()
        player = Player(firstRoom)
        mapBuilder.player = player
        gameDataManager = GameDataManager(player, mapBuilder)

        //Add extra actions
        actions.add(Save(gameDataManager))
        actions.add(Load(gameDataManager))
        actions.add(helpAction)
        populateActionsByNames()
    }

    private fun populateActionsByNames() {
        for (action in actions) {
            actionsByName[action.name] = action
        }
    }

    fun run() {
        println(getIntro())
        do {
            val command = ioHandler.readCommand()
            val commandResult = executeCommand(command)
            var messageToPrint = commandResult.message
            if (commandResult.gameResultCode == GameResultCode.GAME_OVER) {
                messageToPrint += " [Game Over]"
            }
            ioHandler.printMessage(messageToPrint)
        } while (commandResult.gameResultCode != GameResultCode.GAME_OVER)

        for (thread in gameThreads) {
            thread.interrupt()
        }
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
        val action = actionsByName[actionName] ?: return GameResult(GameResultCode.ERROR, "Action [$actionName] not found.")
        val args = parsedCommand.subList(1, parsedCommand.size)
        return action.execute(player, args)
    }
}