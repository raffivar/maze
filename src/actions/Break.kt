package actions

import game.GameResult
import game.GameResultCode
import game.Player

class Break : Action("Break", "Break [item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose an item in the room to break")
        }
        val itemName = args[0]
        val item = player.inventory[itemName] ?: player.currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL,"No [$itemName] in inventory or current room"

        )
        return item.breakItem(player)
    }
}