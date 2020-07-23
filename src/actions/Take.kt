package actions

import game.GameResult
import game.Player
import game.GameResultCode
import items.Takable

class Take : Action("Take", "Take [item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(GameResultCode.ERROR, "Please choose an item in the room to take")
        }
        val itemName = args[0]
        val item = player.currentRoom.items[itemName] ?: return GameResult(
            GameResultCode.FAIL, "No [$itemName] in current room"
        )
        if (item !is Takable) {
            return GameResult(GameResultCode.FAIL, "[${item.name}] is not something you can take")
        }
        player.inventory[item.name] = item
        player.currentRoom.removeItem(item)
        return GameResult(GameResultCode.SUCCESS, "Obtained [${item.name}]")
    }
}