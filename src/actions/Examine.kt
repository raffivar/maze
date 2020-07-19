package actions

import game.GameResult
import game.GameResultCode
import game.Player

class Examine : Action("Examine", "Examine [room/item]") {
    override fun execute(player: Player, args: List<String>): GameResult {
        if (args.isNullOrEmpty()) {
            return GameResult(
                GameResultCode.ERROR,
                "Please choose either the room or an item to examine"
            )
        }
        val objectToExamine = args[0]

        if (objectToExamine == "room") {
            return GameResult(
                GameResultCode.SUCCESS,
                player.currentRoom.getDescription()
            )
        }

        var item = player.inventory[objectToExamine]
        if (item != null) {
            return item.examine()
        }
        item = player.currentRoom.items[objectToExamine]
        if (item != null) {
            return item.examine()
        }

        return GameResult(
            GameResultCode.ERROR,
            "Item [$objectToExamine] does not exist in your inventory or in the current room"
        )
    }

}