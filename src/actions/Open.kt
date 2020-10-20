package actions

import game.GameResult
import game.Player
import game.GameResultCode
import items.Openable

class Open : Action("Open", "Open [item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose an item in the room to open.")
        }
        val itemName = args[0]
        val itemToOpen = player.currentRoom.items[itemName] ?: return GameResult(GameResultCode.FAIL, "No [$itemName] in current room.")
        if (itemToOpen !is Openable) {
            return GameResult(GameResultCode.FAIL, "The [${itemToOpen.name}] is not something you can open.")
        }
        if (!itemToOpen.isClosed) {
            return GameResult(GameResultCode.FAIL, "The [${itemToOpen.name}] is already open.")
        }
        return itemToOpen.open(player)
    }
}