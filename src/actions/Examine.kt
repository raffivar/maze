package actions

import game.GameResult
import game.GameResultCode
import game.Player

class Examine : Action("Examine") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(
                GameResultCode.ERROR,
                "Please choose either the room or an item to examine"
            )
        }
        val itemName = args[0]
        if (itemName == "room") {
            return GameResult(
                GameResultCode.OK,
                player.currRoom.getDescription()
            )
        }
        var item = player.inventory[itemName]
        if (item != null) {
            return GameResult(GameResultCode.OK, item.desc)
        }
        item = player.currRoom.items[itemName]
        if (item != null) {
            return GameResult(GameResultCode.OK, item.desc)
        }
        return GameResult(
            GameResultCode.OK,
            "Item [$itemName] does not exist in your inventory or in the current room"
        )
    }

}