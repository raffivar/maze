package actions

import game.GameResult
import game.GameResultCode
import game.Player

class Use : Action("Use", "Use [item1] on [item2]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty() || args.size < 3 || !args[1].equals("on", true)) {
            return GameResult(GameResultCode.ERROR, "Invalid arguments. Please use the format: '$howToUse'")
        }

        val item1 = args[0]
        val itemToUse = player.inventory[item1] ?: return GameResult(
            GameResultCode.SUCCESS,
            "Item [$item1] does not exist in your inventory"
        )

        val item2 = args[2]
        var itemUsedOn = player.inventory[item2]
        if (itemUsedOn != null) {
            return player.use(itemToUse, itemUsedOn)
        }
        itemUsedOn = player.currentRoom.items[item2]
        if (itemUsedOn != null) {
            return player.use(itemToUse, itemUsedOn)
        }

        return GameResult(
            GameResultCode.ERROR,
            "Item [$itemUsedOn] does not exist in your inventory or in the current room"
        )
    }
}