package actions

import game.GameResult
import game.Player
import game.GameResultCode
import items.Openable

class Open : Action("Open") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose an item in the room to open")
        }

        val itemName = args[0]
        val item = player.currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL,
            "Item [$itemName] does not exist in the current room"
        )

        return player.open(item)
    }
}