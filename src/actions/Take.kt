package actions

import game.GameResult
import game.Player
import game.GameResultCode
import items.Takable

class Take : Action("Take") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(
                GameResultCode.ERROR,
                "Please choose an item in the room to take"
            )
        }
        val itemName = args[0]
        val item = player.currentRoom.items[itemName]
            ?: return GameResult(
                GameResultCode.FAIL,
                "Item [$itemName] does not exist in the current room"
            )
        if (item !is Takable) {
            return GameResult(
                GameResultCode.FAIL,
                "Item [${item.name}] is not something you can take!"
            )
        }
        player.take(item)
        return GameResult(
            GameResultCode.SUCCESS,
            "Obtained [${item.name}]"
        )
    }
}