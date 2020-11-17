package actions

import game.GameResult
import game.GameResultCode
import player.Player
import items.interfaces.UsableWithoutTaking

class Use : Action("Use", "Use [item1] on [item2]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty() || args.size < 3 || !args[1].equals("on", true)) {
            return GameResult(GameResultCode.ERROR, "Invalid arguments. Please use the format: '$howToUse'.")
        }

        val item1Name = args[0]
        val item2Name = args[2]

        val item1 = player.inventory[item1Name] ?: player.currentRoom.items[item1Name] ?: return GameResult(
            GameResultCode.FAIL,
            "No [$item1Name] in inventory or current room."
        )

        val item2 = player.inventory[item2Name] ?: player.currentRoom.items[item2Name] ?: return GameResult(
            GameResultCode.FAIL,
            "No [$item2Name] in inventory or current room."
        )

        if (!player.inventory.containsKey(item1.name) && item1 !is UsableWithoutTaking) {
            return GameResult(GameResultCode.FAIL, "Try taking the [$item1Name] first.")
        }

        return item1.useOn(player, item2)
    }
}