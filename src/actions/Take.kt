package actions

import game.GameResult
import game.Player
import game.GameResultCode
import items.Takable

class Take(args: List<String>) : Action("Take", args) {
    override fun execute(player: Player): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(
                GameResultCode.ERROR,
                "Please choose an item in the room to take"
            )
        }
        val itemName = args[0]
        val item = player.currRoom.items[itemName]
            ?: return GameResult(
                GameResultCode.OK,
                "Item [$itemName] does not exist in the current room"
            )
        return if (item is Takable) {
            player.take(item)
            GameResult(
                GameResultCode.OK,
                "Obtained [${item.name}]"
            )
        } else {
            GameResult(
                GameResultCode.OK,
                "Item [${item.name}] is not something you can take!"
            )
        }
    }
}